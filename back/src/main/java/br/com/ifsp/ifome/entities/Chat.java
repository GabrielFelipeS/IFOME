package br.com.ifsp.ifome.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq")
    @SequenceGenerator(name = "chat_seq", sequenceName = "chat_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder customerOrder;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    public Chat(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

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
