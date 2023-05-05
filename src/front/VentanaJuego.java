package front;

import back.Next;
import back.Nodo;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class VentanaJuego extends javax.swing.JFrame {

    public static int alto;
    public static int ancho;
    private JButton[][] Botones;
    private Nodo[][] Nodos;
    private Nodo InicioNodo;
    private Nodo FinNodo;
    private JButton siguienteButton;
    private int auxFinal;
    private int IndiceActual = 0;
    private int PasoActual = 0;
    private boolean Finalizo = false;
    private Nodo[] CaminoCorto = new Nodo[10 * 10];
    private List<Next> next = new ArrayList<>();
    private boolean botonDerechoPresionado = false;

    public VentanaJuego() {
        initComponents();
        this.setLocationRelativeTo(null);
        alto = (int) spinnerGridY.getValue();
        ancho = (int) spinnerGridX.getValue();
        Nodos = new Nodo[alto + 2][ancho + 2];
        Botones = new JButton[alto + 2][ancho + 2];
        pnlGrid.setLayout(new java.awt.GridLayout(alto + 2, ancho + 2));
        pnlJuego.setOpaque(false);
        pnlGrid.setOpaque(false);
        pnlInstrucciones.setOpaque(false);

        int auxAlto = alto + 2;
        int auxAncho = ancho + 2;

        ButtonHandler handler = new ButtonHandler();

        for (int fila = 0; fila < alto + 2; fila++) {
            for (int col = 0; col < ancho + 2; col++) {
                Nodos[fila][col] = new Nodo(fila, col);
                Botones[fila][col] = new JButton();
                Botones[fila][col].addActionListener(handler);
                if ((col <= auxAncho && (fila == 0 || fila == auxAlto - 1)) || (col == 0 || col == auxAncho - 1)) {
                    Botones[fila][col].setBackground(Color.BLACK);
                    Botones[fila][col].setVisible(false);
                } else {

                    Botones[fila][col].setBackground(new java.awt.Color(255, 255, 255));
                    Botones[fila][col].setOpaque(false);
                    Botones[fila][col].setBorderPainted(true);
                    Botones[fila][col].setContentAreaFilled(false);
                    Botones[fila][col].setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                }

                final int FilaFinal = fila;
                final int ColumnaFinal = col;

                Botones[fila][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            botonDerechoPresionado = true;
                            Botones[FilaFinal][ColumnaFinal].setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/asteroide.png")));
                            Botones[FilaFinal][ColumnaFinal].setBackground(Color.BLACK);
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            botonDerechoPresionado = false;
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (botonDerechoPresionado) {
                            Botones[FilaFinal][ColumnaFinal].setBackground(Color.BLACK);
                            Botones[FilaFinal][ColumnaFinal].setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/asteroide.png")));
                        }
                    }
                });
                pnlGrid.add(Botones[fila][col]);
            }
        }

    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int row = 0; row < alto + 2; row++) {
                for (int col = 0; col < ancho + 2; col++) {
                    if (e.getSource() == Botones[row][col]) {
                        if (InicioNodo == null) {
                            InicioNodo = Nodos[row][col];
                            Botones[row][col].setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/nave.png")));

                            Botones[row][col].setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
                        } else if (FinNodo == null) {
                            FinNodo = Nodos[row][col];
                            Botones[row][col].setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/planeta.png")));
                            Botones[row][col].setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                            dijkstra();
                        } else {
                            reset();
                        }
                    }
                }
            }
        }
    }

    public void cargarVisual() {
        alto = (int) spinnerGridY.getValue();
        ancho = (int) spinnerGridX.getValue();
        Nodos = new Nodo[alto + 2][ancho + 2];
        Botones = new JButton[alto + 2][ancho + 2];

//        setBackground(new java.awt.Color(7, 242, 19));
        pnlGrid.setLayout(new java.awt.GridLayout(alto + 2, ancho + 2));
        ButtonHandler handler = new ButtonHandler();

        int auxAncho = ancho + 2;
        int auxAlto = alto + 2;

        for (int fila = 0; fila < alto + 2; fila++) {
            for (int col = 0; col < ancho + 2; col++) {
                Nodos[fila][col] = new Nodo(fila, col);
                Botones[fila][col] = new JButton();
                Botones[fila][col].addActionListener(handler);
                if ((col <= auxAncho && (fila == 0 || fila == auxAlto - 1)) || (col == 0 || col == auxAncho - 1)) {
                    Botones[fila][col].setBackground(Color.BLACK);
                    Botones[fila][col].setVisible(false);

                } else {
                    Botones[fila][col].setBackground(new java.awt.Color(255, 255, 255));
                    Botones[fila][col].setOpaque(false);
                    Botones[fila][col].setBorderPainted(true);
                    Botones[fila][col].setContentAreaFilled(false);
                    Botones[fila][col].setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

                }
                final int FilaFinal = fila;
                final int ColumnaFinal = col;

                Botones[fila][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            botonDerechoPresionado = true;
                            Botones[FilaFinal][ColumnaFinal].setBackground(Color.BLACK);
                            Botones[FilaFinal][ColumnaFinal].setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/asteroide.png")));
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            botonDerechoPresionado = false;
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (botonDerechoPresionado) {
                            Botones[FilaFinal][ColumnaFinal].setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/asteroide.png")));
                            Botones[FilaFinal][ColumnaFinal].setBackground(Color.BLACK);
                        }
                    }
                });

                pnlGrid.add(Botones[fila][col]);
            }
        }
    }

    private void reset() {
        // Reiniciar todos los botones a su estado original
        int auxAncho = ancho + 2;
        int auxAlto = alto + 2;
        for (int row = 0; row < alto + 2; row++) {
            for (int col = 0; col < ancho + 2; col++) {
                Nodos[row][col].Visto = false;
                Nodos[row][col].Distancia = Integer.MAX_VALUE;
                Nodos[row][col].Anterior = null;
                if ((col <= auxAncho && (row == 0 || row == auxAlto - 1)) || (col == 0 || col == auxAncho - 1)) {
                    Botones[row][col].setBackground(Color.BLACK);
                } else {
                    Botones[row][col].setBackground(new java.awt.Color(255, 255, 255));
                    Botones[row][col].setOpaque(false);
                    Botones[row][col].setBorderPainted(true);
                    Botones[row][col].setContentAreaFilled(false);
                    Botones[row][col].setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                    Botones[row][col].setIcon(null);
                }
            }
        }
        InicioNodo = null;
        FinNodo = null;

        // Reiniciar las variables utilizadas para la animación
        Finalizo = false;
        IndiceActual = 0;
        PasoActual = 0;
        CaminoCorto = new Nodo[alto + 2 * ancho + 2];
        next.clear();
    }

    public void limpiarCulos() {
        for (int i = 0; i < alto + 2; i++) {
            for (int j = 0; j < ancho + 2; j++) {
                pnlGrid.remove(this.Botones[i][j]);
            }
        }
        pnlGrid.updateUI();
        cargarVisual();
    }

    private void dijkstra() {
        PriorityQueue<Nodo> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.Distancia));
        InicioNodo.Distancia = 0;
        queue.add(InicioNodo);
        Finalizo = false;
        IndiceActual = 0;
        next.clear();
        CaminoCorto = new Nodo[alto + 2 * ancho + 2];

        while (!queue.isEmpty()) {
            
            Nodo actual = queue.poll();
            actual.Visto = true;
            if (!(actual == InicioNodo || actual == FinNodo)) {
                next.add(new Next(actual.Fila, actual.columna));
            }
            if (actual == FinNodo) {
                obtenerRuta();
                Finalizo = true;
                break;
            }
            for (int[] direction : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
                int nuevaFila = actual.Fila + direction[0];
                int nuevaColumna = actual.columna + direction[1];
                Nodo nodoVecino = Nodos[nuevaFila][nuevaColumna];
                if (nuevaFila < 0 || nuevaFila >= alto + 2 || nuevaColumna < 0 || nuevaColumna >= ancho + 2) {
                    continue;
                }
                if (Botones[nuevaFila][nuevaColumna].getBackground() == Color.BLACK) {
                    continue;
                }
                if (!nodoVecino.Visto && actual.Distancia + 1 < nodoVecino.Distancia) {
                    nodoVecino.Distancia = actual.Distancia + 1;
                    nodoVecino.Anterior = actual;
                    queue.add(nodoVecino);
                }
            }
        }
        Nodo node = FinNodo;
        int i = 0;
        while (node != null) {
            CaminoCorto[i++] = node;
            node = node.Anterior;
            auxFinal = i;
        }
    }

    private void obtenerRuta() {
        Nodo actual = FinNodo;
        int indice = 0;
        while (actual.Anterior != null) {
            CaminoCorto[indice++] = actual;
            actual = actual.Anterior;
        }
        CaminoCorto[indice] = InicioNodo;
        Finalizo = true;
        IndiceActual = 0;
    }

    public void ant() {
        if (IndiceActual > 0) {
            Nodo current = CaminoCorto[IndiceActual];
            if (Botones[current.Fila][current.columna].getBackground() != Color.RED && Botones[current.Fila][current.columna].getBackground() != Color.GREEN) {
                Botones[current.Fila][current.columna].setOpaque(false);
                Botones[current.Fila][current.columna].setBorderPainted(true);
                Botones[current.Fila][current.columna].setContentAreaFilled(false);
                Botones[current.Fila][current.columna].setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                Botones[current.Fila][current.columna].setIcon(null);
                IndiceActual--;
                btnAvanzar.setEnabled(true);
            }
        }
        Finalizo = false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFondo = new javax.swing.JPanel();
        pnlSuperior = new javax.swing.JPanel();
        pnlSub = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pnlTitulo = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        pnlDatosJuego = new javax.swing.JPanel();
        pnlSize = new javax.swing.JPanel();
        lblGridSizeX = new javax.swing.JLabel();
        spinnerGridX = new javax.swing.JSpinner();
        lblGridSizeY = new javax.swing.JLabel();
        spinnerGridY = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        btnJugar = new javax.swing.JButton();
        pnlJuego = new javax.swing.JPanel();
        pnlGrid = new javax.swing.JPanel();
        pnlInstrucciones = new javax.swing.JPanel();
        lblInstrucciones2 = new javax.swing.JLabel();
        lblInstrucciones = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();
        pnlBotones = new javax.swing.JPanel();
        btnRetroceder = new javax.swing.JButton();
        btnAvanzar = new javax.swing.JButton();
        btnRetrocederTodo = new javax.swing.JButton();
        btnAvanzarTodo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dijkstra");
        setResizable(false);

        pnlFondo.setBackground(new java.awt.Color(0, 0, 0));

        pnlSuperior.setBackground(new java.awt.Color(0, 0, 0));
        pnlSuperior.setLayout(new java.awt.GridLayout(2, 1));

        pnlSub.setBackground(new java.awt.Color(0, 0, 0));
        pnlSub.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 2));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/nave.png"))); // NOI18N
        pnlSub.add(jLabel3);

        pnlTitulo.setBackground(new java.awt.Color(0, 0, 0));
        pnlTitulo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 1));

        lblTitulo.setBackground(new java.awt.Color(0, 0, 0));
        lblTitulo.setFont(new java.awt.Font("Cascadia Code", 1, 60)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("DIJKSTRA");
        pnlTitulo.add(lblTitulo);

        pnlSub.add(pnlTitulo);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/nave.png"))); // NOI18N
        pnlSub.add(jLabel2);

        pnlSuperior.add(pnlSub);

        lblSub.setBackground(new java.awt.Color(0, 0, 0));
        lblSub.setFont(new java.awt.Font("Cascadia Code", 1, 24)); // NOI18N
        lblSub.setForeground(new java.awt.Color(255, 255, 255));
        lblSub.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSub.setText("¿Cuál es el camino más corto?");
        pnlSuperior.add(lblSub);

        pnlDatosJuego.setBackground(new java.awt.Color(0, 0, 0));

        pnlSize.setBackground(new java.awt.Color(0, 0, 0));
        pnlSize.setLayout(new java.awt.GridLayout(1, 4, 20, 0));

        lblGridSizeX.setBackground(new java.awt.Color(0, 0, 0));
        lblGridSizeX.setFont(new java.awt.Font("Cascadia Code", 1, 18)); // NOI18N
        lblGridSizeX.setForeground(new java.awt.Color(255, 255, 255));
        lblGridSizeX.setText("Grid Size X:");
        pnlSize.add(lblGridSizeX);

        spinnerGridX.setFont(new java.awt.Font("Cascadia Code", 1, 15)); // NOI18N
        spinnerGridX.setModel(new javax.swing.SpinnerNumberModel(10, 10, 25, 1));
        pnlSize.add(spinnerGridX);

        lblGridSizeY.setBackground(new java.awt.Color(0, 0, 0));
        lblGridSizeY.setFont(new java.awt.Font("Cascadia Code", 1, 18)); // NOI18N
        lblGridSizeY.setForeground(new java.awt.Color(255, 255, 255));
        lblGridSizeY.setText("Grid Size Y:");
        pnlSize.add(lblGridSizeY);

        spinnerGridY.setFont(new java.awt.Font("Cascadia Code", 1, 15)); // NOI18N
        spinnerGridY.setModel(new javax.swing.SpinnerNumberModel(10, 10, 25, 1));
        pnlSize.add(spinnerGridY);

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Cascadia Code", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Selecciona el numero de filas y columnas:");

        btnJugar.setBackground(new java.awt.Color(0, 0, 0));
        btnJugar.setFont(new java.awt.Font("Cascadia Code", 1, 18)); // NOI18N
        btnJugar.setForeground(new java.awt.Color(255, 255, 255));
        btnJugar.setText("¡JUGAR!");
        btnJugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDatosJuegoLayout = new javax.swing.GroupLayout(pnlDatosJuego);
        pnlDatosJuego.setLayout(pnlDatosJuegoLayout);
        pnlDatosJuegoLayout.setHorizontalGroup(
            pnlDatosJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosJuegoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDatosJuegoLayout.createSequentialGroup()
                        .addComponent(pnlSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosJuegoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnJugar)
                        .addGap(242, 242, 242))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosJuegoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(53, 53, 53))
        );
        pnlDatosJuegoLayout.setVerticalGroup(
            pnlDatosJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosJuegoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(pnlSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnJugar)
                .addGap(22, 22, 22))
        );

        pnlJuego.setBackground(new java.awt.Color(0, 0, 0));
        pnlJuego.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlGrid.setLayout(new java.awt.GridLayout(1, 0));
        pnlJuego.add(pnlGrid, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 44, 720, 420));

        pnlInstrucciones.setBackground(new java.awt.Color(0, 0, 0));
        pnlInstrucciones.setLayout(new java.awt.GridLayout(2, 1));

        lblInstrucciones2.setFont(new java.awt.Font("Cascadia Code", 1, 18)); // NOI18N
        lblInstrucciones2.setForeground(new java.awt.Color(255, 255, 255));
        lblInstrucciones2.setText("Clic derecho para colocar obstáculos.");
        pnlInstrucciones.add(lblInstrucciones2);

        lblInstrucciones.setFont(new java.awt.Font("Cascadia Code", 1, 18)); // NOI18N
        lblInstrucciones.setForeground(new java.awt.Color(255, 255, 255));
        lblInstrucciones.setText("Clic izquierdo para seleccionar inicio y final.");
        pnlInstrucciones.add(lblInstrucciones);

        pnlJuego.add(pnlInstrucciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 662, -1));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/universo.jpg"))); // NOI18N
        pnlJuego.add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 46, 720, 440));

        pnlBotones.setBackground(new java.awt.Color(0, 0, 0));

        btnRetroceder.setBackground(new java.awt.Color(0, 0, 0));
        btnRetroceder.setFont(new java.awt.Font("Cascadia Code", 1, 15)); // NOI18N
        btnRetroceder.setForeground(new java.awt.Color(255, 255, 255));
        btnRetroceder.setText("Retroceder");
        btnRetroceder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetrocederActionPerformed(evt);
            }
        });
        pnlBotones.add(btnRetroceder);

        btnAvanzar.setBackground(new java.awt.Color(0, 0, 0));
        btnAvanzar.setFont(new java.awt.Font("Cascadia Code", 1, 15)); // NOI18N
        btnAvanzar.setForeground(new java.awt.Color(255, 255, 255));
        btnAvanzar.setText("Avanzar");
        btnAvanzar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAvanzarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnAvanzar);

        btnRetrocederTodo.setBackground(new java.awt.Color(0, 0, 0));
        btnRetrocederTodo.setFont(new java.awt.Font("Cascadia Code", 1, 15)); // NOI18N
        btnRetrocederTodo.setForeground(new java.awt.Color(255, 255, 255));
        btnRetrocederTodo.setText("Retroceder Todo");
        btnRetrocederTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetrocederTodoActionPerformed(evt);
            }
        });
        pnlBotones.add(btnRetrocederTodo);

        btnAvanzarTodo.setBackground(new java.awt.Color(0, 0, 0));
        btnAvanzarTodo.setFont(new java.awt.Font("Cascadia Code", 1, 15)); // NOI18N
        btnAvanzarTodo.setForeground(new java.awt.Color(255, 255, 255));
        btnAvanzarTodo.setText("Avanzar Todo");
        btnAvanzarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAvanzarTodoActionPerformed(evt);
            }
        });
        pnlBotones.add(btnAvanzarTodo);

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 754, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(pnlDatosJuego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(pnlJuego, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(pnlSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDatosJuego, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(pnlJuego, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnJugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugarActionPerformed
        limpiarCulos();


    }//GEN-LAST:event_btnJugarActionPerformed

    private void btnAvanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvanzarActionPerformed
        for (int i = 0; i <= 5; i++) {
            System.out.println(auxFinal);
            if (PasoActual < next.size()) {
                Next step = next.get(PasoActual++);
                if (Botones[step.getX()][step.getY()].getBackground() != Color.YELLOW && Botones[step.getX()][step.getY()].getBackground() != Color.RED) {

                    Botones[step.getX()][step.getY()].setBackground(new java.awt.Color(4, 134, 214));
                    Botones[step.getX()][step.getY()].setOpaque(false);
                    Botones[step.getX()][step.getY()].setBorderPainted(true);
                    Botones[step.getX()][step.getY()].setContentAreaFilled(false);
                    Botones[step.getX()][step.getY()].setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
                }

            } else {
                siguiente();
                System.out.println(IndiceActual);

            }

        }
    }//GEN-LAST:event_btnAvanzarActionPerformed

    private void btnRetrocederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetrocederActionPerformed
        for (int i = 5; i >= 0; i--) {
            ant();
            System.out.println(IndiceActual);
            if (PasoActual > 0 && IndiceActual == 0) {
                Next step = next.get(--PasoActual);
                Botones[step.getX()][step.getY()].setOpaque(false);
                Botones[step.getX()][step.getY()].setBorderPainted(true);
                Botones[step.getX()][step.getY()].setContentAreaFilled(false);
                Botones[step.getX()][step.getY()].setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                Botones[step.getX()][step.getY()].setIcon(null);
            }

        }
    }//GEN-LAST:event_btnRetrocederActionPerformed

    private void btnAvanzarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvanzarTodoActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i <= IndiceActual + next.size(); i++) {
            System.out.println(IndiceActual);
            if (PasoActual < next.size()) {
                Next step = next.get(PasoActual++);
                if (Botones[step.getX()][step.getY()].getBackground() != Color.YELLOW && Botones[step.getX()][step.getY()].getBackground() != Color.RED) {

                    Botones[step.getX()][step.getY()].setBackground(new java.awt.Color(4, 134, 214));
                    Botones[step.getX()][step.getY()].setOpaque(false);
                    Botones[step.getX()][step.getY()].setBorderPainted(true);
                    Botones[step.getX()][step.getY()].setContentAreaFilled(false);
                    Botones[step.getX()][step.getY()].setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
          
                }
               
            } else {
                siguiente();
                System.out.println(IndiceActual);
            }
        }
    }//GEN-LAST:event_btnAvanzarTodoActionPerformed

    private void btnRetrocederTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetrocederTodoActionPerformed
        for (int i = IndiceActual + next.size(); i >= 0; i--) {
            ant();
            System.out.println(IndiceActual);
            if (PasoActual > 0 && IndiceActual == 0) {
                Next step = next.get(--PasoActual);
                Botones[step.getX()][step.getY()].setOpaque(false);
                Botones[step.getX()][step.getY()].setBorderPainted(true);
                Botones[step.getX()][step.getY()].setContentAreaFilled(false);
                Botones[step.getX()][step.getY()].setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                Botones[step.getX()][step.getY()].setIcon(null);
            }
        }
    }//GEN-LAST:event_btnRetrocederTodoActionPerformed

    public void siguiente() {
        if (IndiceActual == auxFinal - 2) {
            siguienteButton.setEnabled(false);
        } else {
            if (IndiceActual <= CaminoCorto.length - 1) {
                Nodo current = CaminoCorto[++IndiceActual];
                if (Botones[current.Fila][current.columna].getBackground() != Color.RED && Botones[current.Fila][current.columna].getBackground() != Color.GREEN) {
                    Botones[current.Fila][current.columna].setBackground(Color.YELLOW);
                    Botones[current.Fila][current.columna].setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/nave.png")));
                    Botones[current.Fila][current.columna].setOpaque(false);
                    Botones[current.Fila][current.columna].setBorderPainted(true);
                    Botones[current.Fila][current.columna].setContentAreaFilled(false);
                    Botones[current.Fila][current.columna].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));
                     
                  
                }
             
                
            }
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAvanzar;
    private javax.swing.JButton btnAvanzarTodo;
    private javax.swing.JButton btnJugar;
    private javax.swing.JButton btnRetroceder;
    private javax.swing.JButton btnRetrocederTodo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblGridSizeX;
    private javax.swing.JLabel lblGridSizeY;
    private javax.swing.JLabel lblInstrucciones;
    private javax.swing.JLabel lblInstrucciones2;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlDatosJuego;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JPanel pnlGrid;
    private javax.swing.JPanel pnlInstrucciones;
    private javax.swing.JPanel pnlJuego;
    private javax.swing.JPanel pnlSize;
    private javax.swing.JPanel pnlSub;
    private javax.swing.JPanel pnlSuperior;
    private javax.swing.JPanel pnlTitulo;
    private javax.swing.JSpinner spinnerGridX;
    private javax.swing.JSpinner spinnerGridY;
    // End of variables declaration//GEN-END:variables
}
