package lotto.domain.dao;

import lotto.domain.dto.PaymentInfoDTO;

import java.sql.*;

import static lotto.domain.dao.sqls.PaymentDaoSqls.INSERT_PAYMENT_INFO;
import static lotto.domain.dao.utils.JdbcConnector.getConnection;

public class PaymentInfoDao {
    private PaymentInfoDao(){}

    private static class PaymentInfoDaoHolder {
        private static final PaymentInfoDao INSTANCE = new PaymentInfoDao();
    }

    public static PaymentInfoDao getInstance() {
        return PaymentInfoDaoHolder.INSTANCE;
    }

    public int insertPayment(PaymentInfoDTO paymentInfoDTO) throws SQLDataException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                INSERT_PAYMENT_INFO, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, paymentInfoDTO.getPayment());
            preparedStatement.setInt(2, paymentInfoDTO.getManual());
            preparedStatement.setInt(3, paymentInfoDTO.getAuto());
            preparedStatement.setString(4, paymentInfoDTO.getName());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (!resultSet.next()) {
                    throw new SQLDataException();
                }
                return resultSet.getInt(1);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SQLDataException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLDataException();
        }
    }
}
