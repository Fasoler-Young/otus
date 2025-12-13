package ru.otus;

import io.grpc.ManagedChannelBuilder;
import ru.otus.protobuf.RemoteSequenceServiceGrpc;
import ru.otus.protobuf.SequenceRange;
import ru.otus.service.SequenceClientServiceImpl;

@SuppressWarnings({"squid:S106", "squid:S2142"})
public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) {
        var sequenceClientService = new SequenceClientServiceImpl();
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = RemoteSequenceServiceGrpc.newStub(channel);
        var sequenceRange =
                SequenceRange.newBuilder().setFirstValue(0).setLastValue(30).build();
        stub.getSequence(sequenceRange, sequenceClientService);

        int curValue = 0;
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            curValue = curValue + 1 + sequenceClientService.getCurValueAndClear();
            System.out.println("currentValue: " + curValue);
        }

        sequenceClientService.onCompleted();

        //        var savedUserMsg = stub.saveUser(UserMessage.newBuilder()
        //                .setFirstName("Вася")
        //                .setLastName("Кириешкин")
        //                .build());
        //
        //        System.out.printf(
        //                "Мы сохранили Васю: {id: %d, name: %s %s}%n",
        //                savedUserMsg.getId(), savedUserMsg.getFirstName(), savedUserMsg.getLastName());
        //
        //        var allUsersIterator = stub.findAllUsers(Empty.getDefaultInstance());
        //        System.out.println("Конградулейшенз! Мы получили юзеров! Среди них должен найтись один Вася!");
        //        allUsersIterator.forEachRemaining(
        //                um -> System.out.printf("{id: %d, name: %s %s}%n", um.getId(), um.getFirstName(),
        // um.getLastName()));
        //
        //        System.out.println("\n\n\nА теперь тоже самое, только асинхронно!!!\n\n");
        //        var latch = new CountDownLatch(1);
        //        var newStub = RemoteDBServiceGrpc.newStub(channel);
        //        newStub.findAllUsers(Empty.getDefaultInstance(), new StreamObserver<UserMessage>() {
        //            @Override
        //            public void onNext(UserMessage um) {
        //                System.out.printf("{id: %d, name: %s %s}%n", um.getId(), um.getFirstName(), um.getLastName());
        //            }
        //
        //            @Override
        //            public void onError(Throwable t) {
        //                System.err.println(t.getMessage());
        //            }
        //
        //            @Override
        //            public void onCompleted() {
        //                System.out.println("\n\nЯ все!");
        //                latch.countDown();
        //            }
        //        });
        //
        //        latch.await();

        channel.shutdown();
    }
}
