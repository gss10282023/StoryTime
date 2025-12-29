package group12.storytime.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UUIDTypeHandlerTest {

    private UUIDTypeHandler uuidTypeHandler;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private CallableStatement callableStatement;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        uuidTypeHandler = new UUIDTypeHandler();
    }

    @Test
    public void testSetNonNullParameter() throws SQLException {
        UUID uuid = UUID.randomUUID();
        uuidTypeHandler.setNonNullParameter(preparedStatement, 1, uuid, JdbcType.BINARY);
        verify(preparedStatement, times(1)).setObject(1, uuid);
    }

    @Test
    public void testGetNullableResultByColumnName() throws SQLException {
        UUID expectedUuid = UUID.randomUUID();
        when(resultSet.getObject("column_name", UUID.class)).thenReturn(expectedUuid);

        UUID actualUuid = uuidTypeHandler.getNullableResult(resultSet, "column_name");
        assertEquals(expectedUuid, actualUuid);
    }

    @Test
    public void testGetNullableResultByColumnIndex() throws SQLException {
        UUID expectedUuid = UUID.randomUUID();
        when(resultSet.getObject(1, UUID.class)).thenReturn(expectedUuid);

        UUID actualUuid = uuidTypeHandler.getNullableResult(resultSet, 1);
        assertEquals(expectedUuid, actualUuid);
    }

    @Test
    public void testGetNullableResultFromCallableStatement() throws SQLException {
        UUID expectedUuid = UUID.randomUUID();
        when(callableStatement.getObject(1, UUID.class)).thenReturn(expectedUuid);

        UUID actualUuid = uuidTypeHandler.getNullableResult(callableStatement, 1);
        assertEquals(expectedUuid, actualUuid);
    }

    @Test
    public void testGetNullableResultByColumnName_Null() throws SQLException {
        when(resultSet.getObject("column_name", UUID.class)).thenReturn(null);

        UUID actualUuid = uuidTypeHandler.getNullableResult(resultSet, "column_name");
        assertNull(actualUuid);
    }

    @Test
    public void testGetNullableResultByColumnIndex_Null() throws SQLException {
        when(resultSet.getObject(1, UUID.class)).thenReturn(null);

        UUID actualUuid = uuidTypeHandler.getNullableResult(resultSet, 1);
        assertNull(actualUuid);
    }

    @Test
    public void testGetNullableResultFromCallableStatement_Null() throws SQLException {
        when(callableStatement.getObject(1, UUID.class)).thenReturn(null);

        UUID actualUuid = uuidTypeHandler.getNullableResult(callableStatement, 1);
        assertNull(actualUuid);
    }
}
