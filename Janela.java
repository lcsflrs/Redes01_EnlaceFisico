/* *********************************************************************************
* Autor: Lucas de Oliveira Alves Flores                                            *
* Matricula: 201911916                                                             *
* Inicio: 27 de julho de 2021                                    	                 *
* Ultima alteracao: 03 de agosto de 2021                                           *
* Funcao: Simular Tranmissao e Recepcao de Dados pela Camada Fisica                *
********************************************************************************** */

/* *********************************************************************************
* Classe: Principal                                                                *
* Funcao: Implementar a classe Janela, criando a GUI                               *
********************************************************************************** */

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class Janela extends JFrame {

  /* Declaracoes referentes a interface */
  private JPanel painel;
  private JLabel background, labelCodificacaoTransmissao, labelCodificacaoRecepcao;
  private ImageIcon imagemBackground;
  public JTextArea campoDeTextoPalavra, campoDeTextoCodificado, campoDeTextoDecodificado,
      campoDeTextoDecodificadoPalavra, campoTransmissora, campoReceptora;
  public JScrollPane scrollPanePalavra, scrollPaneCodificado, scrollPaneDecodificado, scrollPaneDecodificadoPalavra,
      scrollTransmissora, scrollReceptora;
  public ButtonGroup grupoDeBotoes;
  public JRadioButton botaoCodificacaoBinaria, botaoCodificacaoManchester, botaoCodificacaoManchesterDiferencial;
  public JPanel painelRadio;
  public JPanel meioDeComunicacao;
  public JLabel[] bits;
  public JButton iniciar;

  /* Declaracao de um Semaforo para auxiliar no inicio da transmissao */
  public static Semaphore semaforo = new Semaphore(0);

  String mensagem;

  public Janela() {

    /*****************************************/
    /* Implementacoes referentes a interface */
    /*****************************************/

    super("Camada Fisica");
    setSize(910, 550);
    setLayout(null);
    setResizable(false);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    painel = new JPanel();
    painel.setSize(910, 550);
    painel.setLayout(null);
    this.add(painel);

    meioDeComunicacao = new JPanel();
    meioDeComunicacao.setLayout(null);
    meioDeComunicacao.setSize(690, 43);
    meioDeComunicacao.setLocation(103, 417);
    painel.add(meioDeComunicacao);

    bits = new JLabel[43];
    for (int i = 0; i < 43; i++) {
      bits[i] = new JLabel();
      bits[i].setSize(43, 42);
      bits[i].setLocation(43 * i, 0);
      meioDeComunicacao.add(bits[i]);
    }

    iniciar = new JButton("Iniciar");
    iniciar.setLocation(398, 300);
    iniciar.setSize(100, 35);
    iniciar.setEnabled(false);
    iniciar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg) {
        botaoCodificacaoBinaria.setEnabled(false);
        botaoCodificacaoManchester.setEnabled(false);
        botaoCodificacaoManchesterDiferencial.setEnabled(false);
        mensagem = JOptionPane.showInputDialog(null, "Digite a mensagem:");
        campoTransmissora.setText("Mensagem: " + mensagem);
        iniciar.setEnabled(false);
        semaforo.release();
      }
    });
    painel.add(iniciar);

    botaoCodificacaoBinaria = new JRadioButton("Codificacao Binaria");
    botaoCodificacaoBinaria.setActionCommand("Coficicacao Binaria");
    botaoCodificacaoBinaria.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg) {
        Principal.tipoDeCodificacao = 0;
        labelCodificacaoRecepcao.setText("Codificacao Binaria");
        labelCodificacaoTransmissao.setText("Codificacao Binaria");
        iniciar.setEnabled(true);
      }
    });

    botaoCodificacaoManchester = new JRadioButton("Codificacao Manchester");
    botaoCodificacaoManchester.setActionCommand("Coficicacao Manchester");
    botaoCodificacaoManchester.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg) {
        Principal.tipoDeCodificacao = 1;
        labelCodificacaoRecepcao.setText("Codificacao Manchester");
        labelCodificacaoTransmissao.setText("Codificacao Manchester");
        iniciar.setEnabled(true);
      }
    });

    botaoCodificacaoManchesterDiferencial = new JRadioButton("Codificacao Manchester Diferencial");
    botaoCodificacaoManchesterDiferencial.setActionCommand("Coficicacao Manchester Diferencial");
    botaoCodificacaoManchesterDiferencial.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg) {
        Principal.tipoDeCodificacao = 2;
        labelCodificacaoRecepcao.setText("Codificacao Manchester Diferencial");
        labelCodificacaoTransmissao.setText("Codificacao Manchester Diferencial");
        iniciar.setEnabled(true);
      }
    });

    grupoDeBotoes = new ButtonGroup();
    grupoDeBotoes.add(botaoCodificacaoBinaria);
    grupoDeBotoes.add(botaoCodificacaoManchester);
    grupoDeBotoes.add(botaoCodificacaoManchesterDiferencial);

    painelRadio = new JPanel(new GridLayout(0, 1));
    painelRadio.add(botaoCodificacaoBinaria);
    painelRadio.add(botaoCodificacaoManchester);
    painelRadio.add(botaoCodificacaoManchesterDiferencial);
    painelRadio.setLocation(347, 88);
    painelRadio.setSize(202, 100);
    painelRadio.setOpaque(false);
    painelRadio.setBackground(new Color(0, 0, 0, 0));
    painel.add(painelRadio);

    campoTransmissora = new JTextArea("");
    campoTransmissora.setFont(new Font("Calibri", 1, 15));
    campoTransmissora.setForeground(Color.blue);
    campoTransmissora.setEditable(false);
    scrollTransmissora = new JScrollPane(campoTransmissora);
    scrollTransmissora.setSize(312, 34);
    scrollTransmissora.setLocation(31, 119);
    painel.add(scrollTransmissora);

    campoReceptora = new JTextArea("");
    campoReceptora.setFont(new Font("Calibri", 1, 15));
    campoReceptora.setForeground(Color.blue);
    campoReceptora.setEditable(false);
    scrollReceptora = new JScrollPane(campoReceptora);
    scrollReceptora.setSize(312, 34);
    scrollReceptora.setLocation(553, 119);
    painel.add(scrollReceptora);

    labelCodificacaoTransmissao = new JLabel("", SwingConstants.CENTER);
    labelCodificacaoTransmissao.setSize(311, 29);
    labelCodificacaoTransmissao.setLocation(33, 270);
    labelCodificacaoTransmissao.setFont(new Font("Calibri", 1, 21));
    labelCodificacaoTransmissao.setForeground(Color.blue);
    labelCodificacaoTransmissao.setVisible(true);
    painel.add(labelCodificacaoTransmissao);

    labelCodificacaoRecepcao = new JLabel("", SwingConstants.CENTER);
    labelCodificacaoRecepcao.setSize(311, 29);
    labelCodificacaoRecepcao.setLocation(555, 270);
    labelCodificacaoRecepcao.setFont(new Font("Calibri", 1, 21));
    labelCodificacaoRecepcao.setForeground(Color.blue);
    labelCodificacaoRecepcao.setVisible(true);
    painel.add(labelCodificacaoRecepcao);

    campoDeTextoPalavra = new JTextArea("");
    campoDeTextoPalavra.setEditable(false);
    scrollPanePalavra = new JScrollPane(campoDeTextoPalavra);
    scrollPanePalavra.setSize(312, 34);
    scrollPanePalavra.setLocation(31, 187);
    painel.add(scrollPanePalavra);

    campoDeTextoCodificado = new JTextArea("");
    campoDeTextoCodificado.setEditable(false);
    scrollPaneCodificado = new JScrollPane(campoDeTextoCodificado);
    scrollPaneCodificado.setSize(312, 34);
    scrollPaneCodificado.setLocation(31, 328);
    painel.add(scrollPaneCodificado);

    campoDeTextoDecodificado = new JTextArea("");
    campoDeTextoDecodificado.setEditable(false);
    scrollPaneDecodificado = new JScrollPane(campoDeTextoDecodificado);
    scrollPaneDecodificado.setSize(312, 34);
    scrollPaneDecodificado.setLocation(553, 328);
    painel.add(scrollPaneDecodificado);

    campoDeTextoDecodificadoPalavra = new JTextArea("");
    campoDeTextoDecodificadoPalavra.setEditable(false);
    scrollPaneDecodificadoPalavra = new JScrollPane(campoDeTextoDecodificadoPalavra);
    scrollPaneDecodificadoPalavra.setSize(312, 34);
    scrollPaneDecodificadoPalavra.setLocation(553, 187);
    painel.add(scrollPaneDecodificadoPalavra);

    imagemBackground = new ImageIcon(getClass().getResource("Background.png"));
    background = new JLabel(imagemBackground);
    background.setSize(910, 550);
    background.setVisible(true);
    painel.add(background);

    this.setVisible(true);
  }
}