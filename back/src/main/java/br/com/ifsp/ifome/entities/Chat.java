package br.com.ifsp.ifome.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder customerOrder;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getCustomerOrderId() {return customerOrder.getId();}

    public Message addMessage(Message message) {
        this.messages.add(message);
        return message;
    }

    public Message addMessage(String content, String loggedEmail, Collection<? extends GrantedAuthority> authorities) {
        String senderType = this.getSenderTypeByAuthorities(authorities);
        Message message = new Message(this, loggedEmail, senderType, content);

        return this.addMessage(message);
    }

    private String getSenderTypeByAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .filter(auth -> auth.startsWith("ROLE_"))
            .findFirst()
            .orElseThrow()
            .replace("ROLE_", "");
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}
