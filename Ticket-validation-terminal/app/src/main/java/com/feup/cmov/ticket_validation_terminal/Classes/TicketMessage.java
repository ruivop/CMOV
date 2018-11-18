package com.feup.cmov.ticket_validation_terminal.Classes;

public class TicketMessage {
    private String ticketId;
    private String eventDate;
    private boolean confirmed;
    private String errorMessage;

    public TicketMessage(String ticketId, String eventDate) {
        this.ticketId = ticketId;
        this.eventDate = eventDate;
        confirmed = false;
        errorMessage = "";
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void writeText(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
