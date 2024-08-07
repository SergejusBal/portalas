package Paskaita_2024_08_05PostPortalas.portalas.Repositories;

import Paskaita_2024_08_05PostPortalas.portalas.Models.Posts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    public String createPost(Posts posts){

        String sql = "INSERT INTO posts (name, content, contacts)\n" +
                "VALUES (?,?,?);";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,posts.getName());
            preparedStatement.setString(2,posts.getContent());
            preparedStatement.setString(3,posts.getContacts());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {

            System.out.println(e.getMessage());

            return "Database connection failed";
        }

        return "Post was successfully added ";

    }

    public List<Posts> getPosts(int limit, int offset){

        List<Posts> postList = new ArrayList<>();

        String sql = "SELECT * FROM posts ORDER BY id desc LIMIT ? OFFSET ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,limit);
            preparedStatement.setInt(2,offset);

            ResultSet resultSet =  preparedStatement.executeQuery();

            while(resultSet.next()) {
               Posts posts = new Posts();
               posts.setId(resultSet.getInt("id"));
               posts.setName(resultSet.getString("name"));
               posts.setContent(resultSet.getString("content"));
               posts.setContacts(resultSet.getString("contacts"));

               LocalDateTime date = formatDateTime(resultSet.getString("data_created"));
               posts.setData_created(date);

               postList.add(posts);
            }

        }catch (SQLException e) {

            System.out.println(e.getMessage());
            return new ArrayList<>();
        }

        return postList;
    }


    private LocalDateTime formatDateTime(String dateTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime rentalDate = null;
        try {
            rentalDate = LocalDateTime.parse(dateTime, dateTimeFormatter);
        }catch(DateTimeParseException | NullPointerException e) {
            rentalDate = LocalDateTime.parse("2000-01-01 00:00:00", dateTimeFormatter);
        }
        return rentalDate;
    }




}
