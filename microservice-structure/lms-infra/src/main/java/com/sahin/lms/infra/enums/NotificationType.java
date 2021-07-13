package com.sahin.lms.infra.enums;

public enum NotificationType {
    RETURN_TIME_CLOSING("{1} remaining to return the book item with member id {2}"),
    RETURN_TIME_TODAY("Today is the day to return the book item with member id {1}"),
    RETURN_TIME_PASSED("{1} passed to return the book item with member id {2}");

    private String content;

    NotificationType(String content) {
        this.content = content;
    }

    private String getContent() {
        return this.content;
    }

    public String getContent(String... params) {
        StringBuilder messageBuilder = new StringBuilder(this.getContent());
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