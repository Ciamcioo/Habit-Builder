package io.github.ciamcioo.habit_builder.entity;

import io.github.ciamcioo.habit_builder.commons.HabitFrequency;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(name = "habit_name", nullable = false)
    private String name;

    @Column(name = "habit_frequency", nullable = false)
    private HabitFrequency frequency;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "reminder")
    private Boolean reminder;

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

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
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

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + uuid +
                ", name='" + name + '\'' +
                ", frequency=" + frequency +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reminder=" + reminder +
                '}';
    }
}
