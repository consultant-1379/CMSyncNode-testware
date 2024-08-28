package com.ericsson.oss.test.notification.clientProducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetIor  {

    public void run() {
        setState(1);
        final List<String> getCmdList = new ArrayList<String>();
        getCmdList.add("GET /cello/ior_files/nameroot.ior HTTP/1.0\r\n\r\n");
        getCmdList
        .add("GET /cello/ior_files/ipv6_nameroot.ior HTTP/1.0\r\n\r\n");
        final Iterator<String> i = getCmdList.iterator();
        do {
            if (!i.hasNext()) {
                break;
            }
            final String GET_CMD = i.next();
            try {
                m_Socket = new Socket(InetAddress.getByName(m_Host), 80);

                m_Socket.getOutputStream().write(GET_CMD.getBytes());
                final BufferedReader in = new BufferedReader(new InputStreamReader(
                        m_Socket.getInputStream()));
                final String httpResult = in.readLine();
                if (httpResult == null) {
                    setResult(false, "Stream closed while reading header");
                    break;
                }
                if (httpResult.indexOf("200 OK") >= 0) {
                    while (in.readLine().length() > 0) {
                        ;
                    }
                    final String ior = in.readLine();
                    setResult(true, ior);
                    break;
                }
                setResult(false, new String(httpResult));
                closeSocket();
            } catch (final UnknownHostException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        } while (true);
        closeSocket();
    }

    synchronized void closeSocket() {
        if (m_Socket != null) {
            try {
                m_Socket.close();
            } catch (final Exception e) {
            }
            m_Socket = null;
        }
    }

    public synchronized String getResult(final long timeout) throws Exception {
        while (m_state == 0) {
            wait();
        }
        wait(timeout);
        if (m_state == 2) {
            return m_Result;
        }
        if (m_Result == null) {
            setState(4);
            closeSocket();
            throw new Exception("Timed out");
        } else {
            return null;
        }
    }

    private synchronized void setResult(final boolean success, final String result) {
        if (m_state == 1) {
            m_Result = result;
            if (success) {
                setState(2);
            } else {
                setState(3);
            }
        }
    }

    private synchronized void setState(final int newState) {
        m_state = newState;
        notify();
    }

    String m_Host;
    Socket m_Socket;
    int m_state;
    String m_Result;

    public GetIor(final String host) {
        m_state = 0;
        m_Host = host;
    }

}
