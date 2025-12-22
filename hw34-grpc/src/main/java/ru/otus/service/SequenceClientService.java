package ru.otus.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.SequenceCurValue;

public interface SequenceClientService extends StreamObserver<SequenceCurValue> {}
