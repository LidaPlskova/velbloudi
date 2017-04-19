package net.sevecek.boot.standalone;

import java.awt.*;
import java.awt.Container;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.List;
import java.util.logging.*;
import java.util.logging.Logger;
import javax.imageio.*;
import javax.servlet.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import org.apache.catalina.*;
import org.apache.catalina.core.*;
import org.apache.catalina.startup.*;
import org.apache.catalina.webresources.*;
import org.apache.tomcat.util.scan.*;
import org.slf4j.*;

public class TomcatApplication {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TomcatApplication.class);

    private Tomcat webServer;
    private JFrame mainWindow;

    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JPanel pnlTop;
    private JLabel labServerState;
    private JLabel labServerStateValue;
    private JLabel labTomcatLogo;
    private WindowLoggingHandler.LoggingListener loggingListener;
    private String webHost;
    private int webPort;
    private JTextField txtAddress;
    private Font largerLabelFont;
    private JLabel labAddress;
    private Font largerTextFieldFont;
    private Color txtAddressBackground;
    private boolean closeRequested;

    public static void run() {
        TomcatApplication instance = new TomcatApplication();
        instance.start();
    }

    public void start() {
        LogManager logManager = LogManager.getLogManager();
        try {
            logManager.readConfiguration(getClass().getResourceAsStream("logging.properties"));
            logManager.getLogger("").log(Level.INFO, "Starting");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        openWebServerManagementWindow();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startServer();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            txtAddress.setText(constructUrl().toString());
                            txtAddress.setEnabled(true);
                            txtAddress.setBackground(txtAddressBackground);
                            labServerStateValue.setText("RUNNING");
                        }
                    });
                } catch (Exception e) {
                    logger.error("Server failed to start", e);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            labServerStateValue.setText("FAILED TO START");
                        }
                    });
                }
            }
        }).start();

        WindowLoggingHandler handler = WindowLoggingHandler.getInstance();
        if (handler != null) {
            synchronized (handler) {
                textArea1.setText(handler.getBuffer());
                loggingListener = new WindowLoggingHandler.LoggingListener() {
                    @Override
                    public void onLog(String text) {
                        try {
                            SwingUtilities.invokeAndWait(() -> {
                                onLogMessage(text);
                            });
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                handler.addLoggingListener(loggingListener);
            }
        }
    }

    private void onLogMessage(String text) {
        String text1 = textArea1.getText();
        text1 = text1 + text;
        textArea1.setText(text1);
    }

    private void openWebServerManagementWindow() {
        mainWindow = new JFrame("Tomcat Web Server");
        mainWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                onWebServerManagementWindowClosing(evt);
            }
        });

        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        pnlTop = new JPanel(new BorderLayout(5, 5));
        labServerState = new JLabel();
        labServerStateValue = new JLabel();
        txtAddress = new JTextField();
        labTomcatLogo = new JLabel();

        //======== this ========
        try {
            List<Image> icons = Arrays.asList(
                    ImageIO.read(getClass().getResource("tomcat-16.png")),
                    ImageIO.read(getClass().getResource("tomcat-20.png")),
                    ImageIO.read(getClass().getResource("tomcat-24.png")),
                    ImageIO.read(getClass().getResource("tomcat-32.png")),
                    ImageIO.read(getClass().getResource("tomcat-40.png")),
                    ImageIO.read(getClass().getResource("tomcat-64.png")),
                    ImageIO.read(getClass().getResource("tomcat-128.png")),
                    ImageIO.read(getClass().getResource("tomcat-256.png")),
                    ImageIO.read(getClass().getResource("tomcat-260.png"))
            );
            mainWindow.setIconImages(icons);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Container contentPane = mainWindow.getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));

        //======== scrollPane1 ========
        {
            scrollPane1.setBorder(new EmptyBorder(5, 5, 5, 5));
            scrollPane1.setViewportBorder(new BevelBorder(BevelBorder.LOWERED));

            //---- textArea1 ----
            textArea1.setBackground(labServerState.getBackground());
            textArea1.setBorder(null);
            textArea1.setFont(textArea1.getFont().deriveFont(textArea1.getFont().getSize() + 10f));
            textArea1.setText("Server status");
            textArea1.setColumns(90);
            textArea1.setRows(18);
            scrollPane1.setViewportView(textArea1);
        }
        contentPane.add(scrollPane1, BorderLayout.CENTER);

        //======== pnlTop ========
        {
            pnlTop.setBorder(new EmptyBorder(5, 10, 5, 5));

            pnlTop.setLayout(new GridBagLayout());
            ((GridBagLayout)pnlTop.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
            ((GridBagLayout)pnlTop.getLayout()).rowHeights = new int[] {0, 0, 0};
            ((GridBagLayout)pnlTop.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 0};
            ((GridBagLayout)pnlTop.getLayout()).rowWeights = new double[] {1.0, 0.0, 0};

            largerLabelFont = labServerStateValue.getFont().deriveFont(labServerStateValue.getFont().getSize() + 5f);
            largerTextFieldFont = txtAddress.getFont().deriveFont(txtAddress.getFont().getSize() + 5f);
            txtAddress.setFont(largerTextFieldFont);
            txtAddressBackground = txtAddress.getBackground();
            txtAddress.setEnabled(false);
            txtAddress.setEditable(false);
            txtAddress.setBackground(txtAddressBackground);

            //---- labServerState ----
            labServerState.setText("Server State:");
            labServerState.setFont(largerLabelFont);
            labServerState.setHorizontalAlignment(SwingConstants.TRAILING);
            pnlTop.add(labServerState, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- labServerStateValue ----
            labServerStateValue.setText("INITIALIZING");
            labServerStateValue.setFont(largerLabelFont);
            pnlTop.add(labServerStateValue, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- labTomcatLogo ----
            labTomcatLogo.setIcon(new ImageIcon(getClass().getResource("/net/sevecek/boot/standalone/tomcat-logo.png")));
            pnlTop.add(labTomcatLogo, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- labAdress ----
            labAddress = new JLabel();
            labAddress.setText("Address for browser:");
            labAddress.setFont(largerLabelFont);
            labAddress.setHorizontalAlignment(SwingConstants.TRAILING);
            pnlTop.add(labAddress, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
            pnlTop.add(txtAddress, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

        }
        contentPane.add(pnlTop, BorderLayout.NORTH);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int maxWidth = (int) (gd.getDisplayMode().getWidth() * 0.8);
        int maxHeight = (int) (gd.getDisplayMode().getHeight() * 0.8);

        mainWindow.pack();
        if (mainWindow.getWidth() < maxWidth) {
            maxWidth = mainWindow.getWidth();
        }
        if (mainWindow.getHeight() < maxHeight) {
            maxHeight = mainWindow.getHeight();
        }
        mainWindow.setSize(maxWidth, maxHeight);
        textArea1.setColumns(1);
        textArea1.setRows(1);

        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }

    private void onWebServerManagementWindowClosing(WindowEvent evt) {
        if (!closeRequested) {
            closeRequested = true;
            labServerStateValue.setText("STOPPING");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        webServer.stop();
                    } catch (LifecycleException e) {
                        throw new RuntimeException(e);
                    } finally {
                        mainWindow.dispose();
                    }
                }
            }).start();
        }
    }

    private void startServer() {
        try {
//            System.setProperty("tomcat.util.scan.StandardJarScanFilter.jarsToSkip", "*");

            webServer = new Tomcat();

            File buildClassesPath = detectBuildClassesPath();
            webServer.setBaseDir(buildClassesPath.getParent());

            webHost = "localhost";
            webPort = detectSparePort();
            webServer.setPort(webPort);

            String webappDirLocation = "web/";
            StandardContext ctx = (StandardContext) webServer.addWebapp("", new File(webappDirLocation).getAbsolutePath());
            logger.info("Starting: " + new File(webappDirLocation).getAbsoluteFile().getParentFile());

            // Declare an alternative location for your "WEB-INF/classes" dir
            // Servlet 3.0 annotation will work
            File webInfClasses = detectBuildClassesPath();
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                    webInfClasses.getAbsolutePath(), "/"));
            ctx.setResources(resources);

            ((StandardJarScanner) ctx.getJarScanner()).setScanClassPath(false);

            webServer.start();

//            openWebBrowser(webHost, webPort);

            logger.info("Tomcat is running");
        } catch (ServletException | LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    private int detectSparePort() {
        List<Integer> candidates = Arrays.asList(80, 8080, 8000, 8888);

        int webPort = -1;
        for (Integer candidatePort : candidates) {
            try {
                ServerSocket serverSocket = new ServerSocket(candidatePort);
                serverSocket.close();
                webPort = candidatePort;
                break;
            } catch (IOException e) {
                // continue
            }
        }

        if (webPort == -1) {
            logger.error("No spare TCP/IP port available for HTTP server");
        }

        return webPort;
    }

    private void openWebBrowser() {
        URI completeUrl = constructUrl();
        try {
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                Runtime.getRuntime().exec("cmd /c start " + completeUrl);
            } else {
                Logger.getAnonymousLogger().info("Opening browser using Desktop");
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(completeUrl);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to open web browser to " + completeUrl, e);
        }
    }

    private URI constructUrl() {
        URI completeUrl = null;
        try {
            if (webPort == 80) {
                completeUrl = new URI(MessageFormat.format("http://{0}/", webHost));
            } else {
                completeUrl = new URI(MessageFormat.format("http://{0}:{1,number,0}/", webHost, webPort));
            }
        } catch (URISyntaxException e) {
            throw new AssertionError("Never happens", e);
        }
        return completeUrl;
    }

    private File detectBuildClassesPath() {
        File buildClassesPath;
        URL buildClassesUrl = this.getClass().getClassLoader().getResource("");
        if (buildClassesUrl != null) {
            try {
                buildClassesPath = new File(buildClassesUrl.toURI());
            } catch (URISyntaxException e) {
                // This never happens
                throw new AssertionError(e);
            }
        } else {
            buildClassesPath = new File("build/classes");
        }
        return buildClassesPath;
    }
}
