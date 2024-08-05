package Paskaita_2024_08_05PostPortalas.portalas.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Posts {
    private int id;
    private String name;
    private String content;
    private String contacts;
    private LocalDateTime data_created;

    public Posts() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getContacts() {
        return contacts;
    }

    public LocalDateTime getData_created() {
        return data_created;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setData_created(LocalDateTime data_created) {
        this.data_created = data_created;
    }
}
