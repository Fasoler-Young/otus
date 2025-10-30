package calculator;

/*
-Xms256m
-Xmx256m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalcDemo {
    private static final Logger log = LoggerFactory.getLogger(CalcDemo.class);

    public static void main(String[] args) {
        log.info("Max Memory (Xmx): {} MB", Runtime.getRuntime().maxMemory() / 1024.0 / 1024.0);
        long maxUsedMemory = 0;
        long counter = 50_000_000;
        var summator = new Summator();
        long startTime = System.currentTimeMillis();

        for (var idx = 0; idx < counter; idx++) {
            var data = new Data(idx);
            summator.calc(data);
            long curUsedMemory =
                    Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            if (maxUsedMemory < curUsedMemory / 1024.0 / 1024.0) {
                maxUsedMemory = (long) (curUsedMemory / 1024.0 / 1024.0);
            }
            if (idx % 10_000_000 == 0) {
                log.info("{} current idx:{}", LocalDateTime.now(), idx);
                log.info(
                        "Used Memory: {} MB",
                        (Runtime.getRuntime().totalMemory()
                                        - Runtime.getRuntime().freeMemory())
                                / 1024.0
                                / 1024.0);
            }
        }

        long delta = System.currentTimeMillis() - startTime;
        log.info("PrevValue:{}", summator.getPrevValue());
        log.info("PrevPrevValue:{}", summator.getPrevPrevValue());
        log.info("SumLastThreeValues:{}", summator.getSumLastThreeValues());
        log.info("SomeValue:{}", summator.getSomeValue());
        log.info("Sum:{}", summator.getSum());
        log.info("spend msec:{}, sec:{}", delta, (delta / 1000));
        log.info("Max Memory used:{} MB", maxUsedMemory);
        log.info("Max Memory (Xmx): {} MB", Runtime.getRuntime().maxMemory() / 1024.0 / 1024.0);
    }
}
