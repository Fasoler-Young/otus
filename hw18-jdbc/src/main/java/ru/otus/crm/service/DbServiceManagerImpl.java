package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.cachehw.MyCacheLong;
import ru.otus.crm.model.Manager;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final MyCacheLong<Manager> managerMyCacheLong;

    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        managerMyCacheLong = new MyCacheLong<>();
    }

    public DbServiceManagerImpl(
            TransactionRunner transactionRunner,
            DataTemplate<Manager> managerDataTemplate,
            MyCacheLong<Manager> managerMyCacheLong) {
        this.managerDataTemplate = managerDataTemplate;
        this.transactionRunner = transactionRunner;
        this.managerMyCacheLong = managerMyCacheLong;
    }

    @Override
    public Manager saveManager(Manager manager) {
        Manager savedMenager = transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            return manager;
        });
        managerMyCacheLong.put(savedMenager.getNo(), savedMenager);
        return savedMenager;
    }

    @Override
    public Optional<Manager> getManager(long no) {
        return Optional.ofNullable(managerMyCacheLong.get(no))
                .or(() -> transactionRunner.doInTransaction(connection -> {
                    var clientOptional = managerDataTemplate.findById(connection, no);
                    log.info("client: {}", clientOptional);
                    return clientOptional;
                }));
    }

    @Override
    public List<Manager> findAll() {
        List<Manager> managers = transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
        });
        managers.forEach(manager -> managerMyCacheLong.put(manager.getNo(), manager));
        return managers;
    }
}
