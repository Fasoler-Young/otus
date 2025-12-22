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

        channel.shutdown();
    }
}
