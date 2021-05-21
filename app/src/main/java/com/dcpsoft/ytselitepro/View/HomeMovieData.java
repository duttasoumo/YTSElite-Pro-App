package com.dcpsoft.ytselitepro.View;

/**
 * Created by Soumya on 10-Jul-16.
 */
public class HomeMovieData {

    private String movieName;
    private String movieID;
    private String movieYear;
    private String movieRate;
    private String movieImgUrl;

    public HomeMovieData() {
        this.movieName="";
        this.movieID="";
        this.movieRate="";
        this.movieRate="";
        this.movieImgUrl="";
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public String getMovieRate() {
        return movieRate;
    }

    public void setMovieRate(String movieRate) {
        this.movieRate = movieRate;
    }

    public String getMovieImgUrl() {
        return movieImgUrl;
    }

    public void setMovieImgUrl(String movieImgUrl) {
        this.movieImgUrl = movieImgUrl;
    }
}
