package model;

import java.sql.Blob;

public class CategoryBean {
  public int id;
  public String name;
  public String description;
  public byte[] picture;

  /**
   * Represents a Category of item that we sell.
   * 
   * @param id The id of the category -- a numeric value used to group items by
   *        category
   * @param name The name of the category
   * @param description A description about what this category holds
   * @param picture A picture that represents the category
   */
  public CategoryBean(int id, String name, String description, byte[] picture) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.picture = picture;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public byte[] getPicture() {
    return picture;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }


}
