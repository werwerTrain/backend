package com.buaa.werwertrip.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Hotel {
    private String id;
    @TableField(value = "city")
    private String arrive_station;
    private String name;
    //private Double grade;
    private Integer stars;
    private Integer likes;
    private String photo;
    private String position;
    private String content;
    private List<Comment> comments;
    private List<Room> rooms;

    private String phone;
    private String setTime;
    private String description;
    private String breakfastDescription;
    private String ageNotion;
    private String checkinTime;
    private String checkoutTime;

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

    public String getBreakfastDescription() {
        return breakfastDescription;
    }

    public void setBreakfastDescription(String breakfastDescription) {
        this.breakfastDescription = breakfastDescription;
    }

    public String getAgeNotion() {
        return ageNotion;
    }

    public void setAgeNotion(String ageNotion) {
        this.ageNotion = ageNotion;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArrive_station() {
        return arrive_station;
    }

    public void setArrive_station(String arrive_station) {
        this.arrive_station = arrive_station;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Double getGrade() {
//        return grade;
//    }
//
//    public void setGrade(Double grade) {
//        this.grade = grade;
//    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
