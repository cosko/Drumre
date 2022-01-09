package com.labos.lab1.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LikedPageList {
    @JsonProperty("data")
    private List<LikedPage> likedPages;

    public LikedPageList(List<LikedPage> likedPages) {
        this.likedPages = likedPages;
    }
    public LikedPageList() {
    }

    public List<LikedPage> getLikedPages() {
        return likedPages;
    }

    public void setLikedPages(List<LikedPage> likedPages) {
        this.likedPages = likedPages;
    }
}
