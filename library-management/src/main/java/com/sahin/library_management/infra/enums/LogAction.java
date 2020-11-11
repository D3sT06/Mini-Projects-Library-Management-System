package com.sahin.library_management.infra.enums;

public enum LogAction {

    SEARCH_BOOK("Searching books by filter"),

    GET_ITEM_BY_BOOK("Getting items by book id {1}"),
    GET_ITEM("Getting the item by barcode {1}"),

    CHECKOUT_ITEM("Checking out the book item with id {1}"),
    RENEW_ITEM("Renewing the book item with id {1}"),
    RETURN_ITEM("Returning the book item with id {1}"),

    RESERVE_ITEM("Reserving the book item with id {1}"),

    AUTHENTICATE("Authenticating");

    private String message;

    LogAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMessage(String... params) {
        StringBuilder messageBuilder = new StringBuilder(this.getMessage());
        if (params == null || params.length == 0)
            return messageBuilder.toString();
        for (int i = 0; i < params.length; i++) {
            String key = "{" + (i + 1) + "}";
            int index = messageBuilder.indexOf(key);
            //message has not any parametric part or params count is more than parametric parts
            if (index == -1)
                break;

            String replaceWithMe = params[i] != null ? params[i] : "";
            messageBuilder.replace(index, index + key.length(), replaceWithMe);
        }
        return messageBuilder.toString();
    }
}
