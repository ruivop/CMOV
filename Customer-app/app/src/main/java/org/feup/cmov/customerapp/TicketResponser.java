package org.feup.cmov.customerapp;

import org.feup.cmov.customerapp.model.Ticket;

import java.util.ArrayList;

public interface TicketResponser {
    void onResponseReceived(ArrayList<Ticket> tickets);
}
