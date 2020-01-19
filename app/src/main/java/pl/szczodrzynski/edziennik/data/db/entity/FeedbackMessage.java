/*
 * Copyright (c) Kacper Ziubryniewicz 2020-1-6
 */

package pl.szczodrzynski.edziennik.data.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "feedbackMessages")
public class FeedbackMessage {

    @PrimaryKey(autoGenerate = true)
    public int messageId;

    public boolean received = false;
    public String fromUser = null;
    public String fromUserName = null;
    public long sentTime = System.currentTimeMillis();
    public String text;

    public FeedbackMessage(boolean received, String text) {
        this.received = received;
        this.sentTime = System.currentTimeMillis();
        this.text = text;
    }

    @Ignore
    public FeedbackMessage() {
    }
}