import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.ImageIcon;

public class Principal {

  public static int tipoDeCodificacao = 0;
  public static Janela janela = new Janela();
  public static LinkedList<Integer> fila = new LinkedList<Integer>();
  public static int quantidadeQuadros = 43;

  public static void main(String args[]) {
    // Chamar a AplicacaoTransmissora();
    AplicacaoTransmissora();
  }

  public static void AplicacaoTransmissora() {
    Scanner ler = new Scanner(System.in);
    String mensagem;
    System.out.println("Digite a mensagem que quer transmitir:");
    mensagem = ler.nextLine();
    CamadaDeAplicacaoTransmissora(mensagem);
  }

  public static void CamadaDeAplicacaoTransmissora(String mensagem) {
    String[] quadro = new String[mensagem.length()];
    for (int i = 0; i < mensagem.length(); i++) {
      char converterAscii = mensagem.charAt(i);
      // System.out.println("\n\nConverterAscii: " + converterAscii);
      int codigoAscii = (int) converterAscii;
      // System.out.println("CodigoAscii: " + codigoAscii);
      String inserir = Integer.toBinaryString(codigoAscii);
      // System.out.println("ToBinaryString: " + Integer.toBinaryString(codigoAscii));
      // // System.out.println("ParseInt: " +
      // Integer.parseInt(Integer.toBinaryString(codigoAscii)));
      if (inserir.length() < 8) {
        // System.out.println("\n");
        while (inserir.length() < 8) {
          // System.out.println("Diferenca a 8 bits: " + (8 - inserir.length()));
          String zero = "0";
          inserir = zero.concat(inserir);
        }
        // System.out.println("Saiu do IF \n");

      }
      // System.out.println("ToBinaryString Com Zero: " + inserir);
      quadro[i] = inserir;
    }

    int[] bits = new int[quadro.length * 8];
    for (int i = 0; i < quadro.length; i++) {
      // System.out.println("\n\nStringAtual: " + quadro[i]);
      for (int k = 0; k < quadro[i].length(); k++) {
        // System.out.println("\n\nIndex: " + k);
        // System.out.println("CharAt: " + quadro[i].charAt(k));
        // System.out.println("Char Get Numeric: " +
        // Character.getNumericValue(quadro[i].charAt(k)));
        bits[i * 8 + k] = Character.getNumericValue(quadro[i].charAt(k));
      }
    }
    /*
     * System.out.println("\n\n"); for (int i = 0; i < quadro.length; i++) {
     * System.out.print(quadro[i]); }
     */

    /*
     * System.out.println("\n\n"); for (int i = 0; i < bits.length; i++) {
     * System.out.print(bits[i]); }
     */

    CamadaFisicaTransmissora(bits);
  }

  public static void CamadaFisicaTransmissora(int bits[]) {
    int[] fluxoBrutoDeBits = new int[bits.length * 2];
    switch (tipoDeCodificacao) {
      case 0:
        fluxoBrutoDeBits = bits;
        break;
      case 1:
        fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchester(bits);
        break;
      case 2:
        fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(bits);
        break;
    }

    MeioDeComunicacao(fluxoBrutoDeBits);
  }

  public static int[] CamadaFisicaTransmissoraCodificacaoManchester(int bits[]) {
    int[] fluxoDeBits = new int[bits.length * 2];

    for (int i = 0; i < bits.length; i++) {
      if (bits[i] == 0) {
        fluxoDeBits[i * 2] = 0;
        fluxoDeBits[(i * 2) + 1] = 1;
      } else {
        fluxoDeBits[i * 2] = 1;
        fluxoDeBits[(i * 2) + 1] = 0;
      }
    }
    /*
     * System.out.println("\n\nCodificacao Manchester :"); for (int i = 0; i <
     * fluxoDeBits.length; i++) { System.out.print(fluxoDeBits[i]); }
     */
    return fluxoDeBits;
  }

  public static int[] CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(int bits[]) {
    int[] fluxoDeBits = new int[bits.length * 2];

    if (bits[0] == 0) {
      fluxoDeBits[0] = 0;
      fluxoDeBits[1] = 1;
    } else {
      fluxoDeBits[0] = 1;
      fluxoDeBits[1] = 0;
    }

    for (int i = 1; i < bits.length; i++) {
      if (bits[i] == 0) {
        if (fluxoDeBits[(i * 2) - 1] == 0) {
          fluxoDeBits[i * 2] = 1;
          fluxoDeBits[(i * 2) + 1] = 0;
        } else {
          fluxoDeBits[i * 2] = 0;
          fluxoDeBits[(i * 2) + 1] = 1;
        }
      } else {
        if (fluxoDeBits[(i * 2) - 1] == 0) {
          fluxoDeBits[i * 2] = 0;
          fluxoDeBits[(i * 2) + 1] = 1;
        } else {
          fluxoDeBits[i * 2] = 1;
          fluxoDeBits[(i * 2) + 1] = 0;
        }
      }
    }
    /*
     * System.out.println("\n\nCodificacao Manchester Diferencial:"); for (int i =
     * 0; i < fluxoDeBits.length; i++) { System.out.print(fluxoDeBits[i]); }
     */
    return fluxoDeBits;
  }

  public static void MeioDeComunicacao(int fluxoBrutoDeBits[]) {
    int[] fluxoTransmissor, fluxoReceptor;

    fluxoTransmissor = fluxoBrutoDeBits;
    fluxoReceptor = new int[fluxoTransmissor.length];

    // System.out.println("\n\nFluxo Transmissor: ");
    /*
     * for (int i = 0; i < fluxoTransmissor.length; i++) {
     * System.out.print(fluxoTransmissor[i]); }
     */

    for (int i = 0; i < fluxoTransmissor.length; i++) {
      fluxoReceptor[i] = fluxoTransmissor[i];
      // Gui
      if (fila.size() < 43) {
        fila.addFirst(fluxoReceptor[i]);
      } else {
        fila.poll();
        fila.addFirst(fluxoReceptor[i]);
      }

      System.out.println(fluxoReceptor[i]);

      try {
        System.out.println("Dormiu");
        Thread.sleep(200);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }

      if (tipoDeCodificacao == 0) {
        for (int k = 0; k < fila.size(); k++) {
          if (fila.get(k) == 0) {
            janela.bits[k].setIcon(new ImageIcon("00.png"));
          } else {
            janela.bits[k].setIcon(new ImageIcon("11.png"));
          }
        }
      } else {
        for (int k = 0; k < fila.size(); k++) {
          if (k == fila.size() - 1) {
            if (fila.get(k) == 0) {
              janela.bits[k].setIcon(new ImageIcon("00.png"));
            } else {
              janela.bits[k].setIcon(new ImageIcon("11.png"));
            }
          } else {
            if (fila.get(k + 1) == 0) { // o anterior e 0
              if (fila.get(k) == 0) {
                janela.bits[k].setIcon(new ImageIcon("00.png"));
              } else {
                janela.bits[k].setIcon(new ImageIcon("10.png"));
              }
            } else { // o anterior e 1
              if (fila.get(k) == 0) {
                janela.bits[k].setIcon(new ImageIcon("01.png"));
              } else {
                janela.bits[k].setIcon(new ImageIcon("11.png"));
              }
            }
          }
          // janela.bits[k].setIcon(new ImageIcon("01.png"));
        }
      }
    }
    for (int i = 0; i < 43; i++) {
      if (fila.size() < 43) {
        fila.addFirst(3);
      } else {
        fila.poll();
        fila.addFirst(3);
      }
    }
    for (int k = 0; k < fila.size(); k++) {
      janela.bits[k].setIcon(new ImageIcon("__.png"));
      try {
        System.out.println("Dormiu");
        Thread.sleep(200);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }

    /*
     * System.out.println("\nFluxo Receptor: "); for (int i = 0; i <
     * fluxoReceptor.length; i++) { System.out.print(fluxoReceptor[i]); }
     */

    CamadaFisicaReceptora(fluxoReceptor);
  }

  public static void CamadaFisicaReceptora(int fluxoBrutoDeBits[]) {
    int[] bits = new int[fluxoBrutoDeBits.length / 2];
    switch (tipoDeCodificacao) {
      case 0:
        bits = fluxoBrutoDeBits;
        break;
      case 1:
        bits = CamadaFisicaReceptoraCodificacaoManchester(fluxoBrutoDeBits);
        break;
      case 2:
        bits = CamadaFisicaReceptoraCodificacaoManchesterDiferencial(fluxoBrutoDeBits);
        break;
    }

    /*
     * System.out.println("\n\nBits chegando na Camada Fisica Receptora: "); for
     * (int i = 0; i < bits.length; i++) { System.out.print(bits[i]); }
     */

    CamadaDeAplicacaoReceptora(bits);
  }

  public static int[] CamadaFisicaReceptoraCodificacaoManchester(int fluxoBrutoDeBits[]) {
    int[] bits = new int[fluxoBrutoDeBits.length / 2];
    for (int i = 0; i < fluxoBrutoDeBits.length / 2; i++) {
      if (fluxoBrutoDeBits[i * 2] == 0 && fluxoBrutoDeBits[(i * 2) + 1] == 1) {
        bits[i] = 0;
      } else {
        bits[i] = 1;
      }
    }

    return bits;
  }

  public static int[] CamadaFisicaReceptoraCodificacaoManchesterDiferencial(int fluxoBrutoDeBits[]) {
    int[] bits = new int[fluxoBrutoDeBits.length / 2];
    int contador = 0;

    if (fluxoBrutoDeBits[0] == 0 && fluxoBrutoDeBits[1] == 1) {
      bits[0] = 0;
    } else {
      bits[0] = 1;
    }
    contador++;

    for (int i = 2; i < fluxoBrutoDeBits.length; i += 2) {
      // System.out.println("\nBit Anterior: " + fluxoBrutoDeBits[i - 1]);
      // System.out.println("Bit Atual: " + fluxoBrutoDeBits[i]);
      if (fluxoBrutoDeBits[i - 1] == 0) {
        if (fluxoBrutoDeBits[i] == 1) {
          bits[contador] = 0;
        } else {
          bits[contador] = 1;
        }
      } else {
        if (fluxoBrutoDeBits[i] == 1) {
          bits[contador] = 1;
        } else {
          bits[contador] = 0;
        }
      }
      // System.out.println("Bit Traduzido: " + bits[contador]);
      contador++;
    }

    return bits;
  }

  public static void CamadaDeAplicacaoReceptora(int bits[]) {
    String aux = "";
    // System.out.println("\n\n");
    for (int i = 0; i < bits.length; i++) {
      // System.out.println("Bit atual:" + bits[i]);
      aux = aux.concat("" + bits[i]);
    }

    String mensagem = "";
    System.out.println("\n\n");
    for (int i = 0; i < bits.length / 8; i++) {
      // System.out.println("\nSubstring Atual: " + aux.substring(i * 8, (i * 8) +
      // 8));
      mensagem = mensagem.concat("" + (char) Integer.parseInt(aux.substring(i * 8, (i * 8) + 8), 2));
    }

    // System.out.println("Mensagem:\n" + mensagem);
    AplicacaoReceptora(mensagem);
  }

  public static void AplicacaoReceptora(String mensagem) {
    System.out.println("A mensagem recebida foi: " + mensagem);
  }
}