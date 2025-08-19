package ispw.foodcare.utils;

import ispw.foodcare.utils.patternobserver.AppointmentEvent;
import ispw.foodcare.utils.patternobserver.AppointmentListener;
import ispw.foodcare.utils.patternobserver.AppointmentSubject;

import java.util.concurrent.*;

public final class InMemoryAppointmentSubject implements AppointmentSubject {
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<AppointmentListener>> map = new ConcurrentHashMap<>();

    /*Iscrizione*/
    @Override public AutoCloseable subscribe(String nutr, AppointmentListener l) {
        map.computeIfAbsent(nutr, k -> new CopyOnWriteArrayList<>()).add(l);
        return () -> unsubscribe(nutr, l);
    }

    /*Disiscrizione*/
    @Override public void unsubscribe(String nutr, AppointmentListener l) {
        var list = map.get(nutr);
        if (list != null) list.remove(l);
    }

    /*Propaga gli eventi quando il controller li notifica*/
    @Override public void notifyNewAppointment(AppointmentEvent e) {
        var list = map.getOrDefault(e.nutritionistUsername(), new CopyOnWriteArrayList<>());
        for (var l : list) {
            try { l.onAppointmentCreated(e); } catch (Exception ignore) {}
        }
    }
}

