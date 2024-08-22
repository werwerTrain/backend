package com.buaa.werwertrip.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Comment {
    @JsonIgnore
    private Integer hotel_id;
    private String name;
    private String time;
    private String content;
    private Double grade;
    private String photo;
    private String place;
    private String room;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    //    public Double getRank() {
//        return rank;
//    }
//
//    public void setRank(Double rank) {
//        this.rank = rank;
//    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }


    public Integer getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(Integer hotel_id) {
        this.hotel_id = hotel_id;
    }

//    public Double getRank_score() {
//        return rank_score;
//    }
//
//    public void setRank_score(Double rank_score) {
//        this.rank_score = rank_score;
//    }

//    public String getPerson() {
//        return person;
//    }
//
//    public void setPerson(String person) {
//        this.person = person;
//    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
