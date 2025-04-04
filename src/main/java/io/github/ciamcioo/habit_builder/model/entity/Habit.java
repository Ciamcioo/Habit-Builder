package io.github.ciamcioo.habit_builder.model.entity;

import io.github.ciamcioo.habit_builder.model.commons.HabitFrequency;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID uuid;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "habit_frequency", nullable = false)
    @Enumerated(EnumType.STRING)
    private HabitFrequency frequency;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "reminder")
    private Boolean reminder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Habit() {
    }

    public Habit(String name, HabitFrequency frequency, LocalDate startDate, LocalDate endDate, Boolean reminder) {
        this.name = name;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reminder = reminder;
    }

    public Habit(UUID uuid, String name, HabitFrequency frequency, LocalDate startDate, LocalDate endDate, Boolean reminder) {
        this.uuid = uuid;
        this.name = name;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reminder = reminder;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HabitFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(HabitFrequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public void setReminder(Boolean reminder) {
        this.reminder = reminder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Habit habit = (Habit) o;
        return Objects.equals(uuid, habit.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    @Override
    public String toString() {
        return "Habit{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", frequency=" + frequency +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reminder=" + reminder +
                ", user=" + user +
                '}';
    }

}
