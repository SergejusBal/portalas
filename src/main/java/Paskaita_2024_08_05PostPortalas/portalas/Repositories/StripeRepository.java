package Paskaita_2024_08_05PostPortalas.portalas.Repositories;

import Paskaita_2024_08_05PostPortalas.portalas.Models.Posts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Repository
public class StripeRepository {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    public int createPayment(UUID uuid,UUID uuidSecret){

        int payment_id = 0;

        String sql = "INSERT INTO payment (payment_uuid, payment_uuid_secret)\n" +
                "VALUES (?,?);";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,uuid.toString());
            preparedStatement.setString(2,uuidSecret.toString());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            payment_id = resultSet.getInt(1);

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return payment_id;
        }
        return payment_id;
    }

    public void setPaymentStatusToTrue(int id, String uuid, String uuidSecret){
        String sql = "UPDATE payment SET payed = ? WHERE id = ? AND payment_uuid = ? AND payment_uuid_secret =?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1,true);
            preparedStatement.setInt(2, id);
            preparedStatement.setString(3, uuid);
            preparedStatement.setString(4, uuidSecret);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
    public void setPaymentStatusToFalse(int id, String uuid){
        String sql = "UPDATE payment SET payed = ? WHERE id = ? AND payment_uuid = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1,false);
            preparedStatement.setInt(2, id);
            preparedStatement.setString(3, uuid);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }





    public boolean ckeckPaymentStatus(int paymentID, String paymentCode){
        boolean paymentStatus = false;
        String sql = "SELECT * FROM payment WHERE id = ? AND payment_uuid = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,paymentID);
            preparedStatement.setString(2,paymentCode);

            ResultSet resultSet =  preparedStatement.executeQuery();
            resultSet.next();

            paymentStatus = resultSet.getBoolean("payed");

            preparedStatement.close();
            connection.close();

        }
        catch (SQLException e) {
        System.out.println(e.getMessage());
            return paymentStatus;
        }

    return paymentStatus;
    }






}
