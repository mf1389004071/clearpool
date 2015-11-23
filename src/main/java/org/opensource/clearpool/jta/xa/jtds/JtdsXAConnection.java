package org.opensource.clearpool.jta.xa.jtds;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

import org.opensource.clearpool.logging.PoolLogger;
import org.opensource.clearpool.logging.PoolLoggerFactory;

import net.sourceforge.jtds.jdbc.XASupport;

public class JtdsXAConnection implements XAConnection {
  private static final PoolLogger LOGGER = PoolLoggerFactory.getLogger(JtdsXAConnection.class);

  private Connection connection;

  private final XAResource resource;
  private final int xaConnectionId;

  public JtdsXAConnection(Connection connection) throws SQLException {
    resource = new JtdsXAResource(this, connection);
    this.connection = connection;
    xaConnectionId = XASupport.xa_open(connection);
  }

  int getXAConnectionID() {
    return xaConnectionId;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return connection;
  }

  @Override
  public void close() throws SQLException {
    try {
      XASupport.xa_close(connection, xaConnectionId);
    } catch (SQLException e) {
      // Swallow it
    }
    if (connection != null) {
      try {
        connection.close();
      } catch (Exception e) {
        LOGGER.error("close connection error: ", e);
      }
    }
  }

  @Override
  public void addConnectionEventListener(ConnectionEventListener listener) {

  }

  @Override
  public void removeConnectionEventListener(ConnectionEventListener listener) {

  }

  @Override
  public void addStatementEventListener(StatementEventListener listener) {

  }

  @Override
  public void removeStatementEventListener(StatementEventListener listener) {

  }

  @Override
  public XAResource getXAResource() throws SQLException {
    return resource;
  }
}
