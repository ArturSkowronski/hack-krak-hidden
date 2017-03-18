package tech.kolektiv.hiddengame.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus<T> {

    private final Subject<T, T> _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(T o) {
        _bus.onNext(o);
    }

    public Observable<T> toObserverable() {
        return _bus.map(o -> o);
    }
}