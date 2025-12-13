package ru.otus.service;

import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.RemoteSequenceServiceGrpc;
import ru.otus.protobuf.SequenceCurValue;
import ru.otus.protobuf.SequenceRange;

public class RemoteSequenceServiceImpl extends RemoteSequenceServiceGrpc.RemoteSequenceServiceImplBase {

    @Override
    public void getSequence(SequenceRange request, StreamObserver<SequenceCurValue> responseObserver) {
        for (int index = 0; index < 30; index++) {
            try {
                if (Context.current().isCancelled()) {
                    System.out.println("Client cancelled the stream at value " + (index - 1));
                    break;
                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            System.out.println("currentValue: " + index);
            responseObserver.onNext(
                    SequenceCurValue.newBuilder().setValue(index).build());
        }
        responseObserver.onCompleted();
    }
}
