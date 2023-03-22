package seedu.fitbook.model.client;

import java.util.Comparator;

/**
 * This class provides a Comparator implementation for comparing two Client objects
 * base on their appointments.
*/
public class ClientAppointmentComparator implements Comparator<Client> {
    @Override
    public int compare(Client c1, Client c2) {
        if (c1.isAppointmentEmpty(c1) && c2.isAppointmentEmpty(c2)) {
            return 0;
        } else if (c1.isAppointmentEmpty(c1)) {
            return -1;
        } else if (c2.isAppointmentEmpty(c2)) {
            return 1;
        } else {
            Appointment c1Appointment = c1.getAppointments().iterator().next();
            Appointment c2Appointment = c2.getAppointments().iterator().next();
            return c1Appointment.compareTo(c2Appointment);
        }
    }
}
