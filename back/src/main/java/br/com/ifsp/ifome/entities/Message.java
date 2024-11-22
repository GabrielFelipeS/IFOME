package br.com.ifsp.ifome.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senderType;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Message(Chat chat, String email, String senderType, String content) {
        this.chat = chat;
        this.email = email;
        this.senderType = senderType;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public String getCreatedAtTimestamp() {
        return Timestamp.valueOf(createdAt).toString();
    }
}
