package tech.kolektiv.newton.rx;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import tech.kolektiv.newton.devices.Place;

public class RxBus {

    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Place> toObserverable() {
        return _bus.map(new Func1<Object, Place>() {

            @Override
            public Place call(Object o) {
                return (Place)o;
            }
        });
    }
}