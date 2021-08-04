import java.util.LinkedList;

import javax.swing.ImageIcon;

public class Principal {

  public static int tipoDeCodificacao = 1;
  public static Janela janela = new Janela();
  public static LinkedList<Integer> fila = new LinkedList<Integer>();
  public static int quantidadeQuadros = 43;

  public static void main(String args[]) {
    AplicacaoTransmissora();
  }

  public static void AplicacaoTransmissora() {
    try {
      janela.semaforo.acquire();
    } catch (InterruptedException ex) {
    }
    CamadaDeAplicacaoTransmissora(janela.mensagem);
  }

  public static void CamadaDeAplicacaoTransmissora(String mensagem) {
    String[] quadro = new String[mensagem.length()];
    for (int i = 0; i < mensagem.length(); i++) {
      char converterAscii = mensagem.charAt(i);
      int codigoAscii = (int) converterAscii;
      String inserir = Integer.toBinaryString(codigoAscii);
      if (inserir.length() < 8) {
        while (inserir.length() < 8) {
          String zero = "0";
          inserir = zero.concat(inserir);
        }
      }
      quadro[i] = inserir;
    }

    String binario = "";
    for (int i = 0; i < quadro.length; i++) {
      binario += quadro[i];
      janela.campoDeTextoPalavra.setText(binario);
      try {
        Thread.sleep(25);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }

    int[] bits = new int[quadro.length * 8];
    for (int i = 0; i < quadro.length; i++) {
      for (int k = 0; k < quadro[i].length(); k++) {
        bits[i * 8 + k] = Character.getNumericValue(quadro[i].charAt(k));
      }
    }

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

    String mensagemCodificada = "";
    for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
      mensagemCodificada += fluxoBrutoDeBits[i];
      janela.campoDeTextoCodificado.setText(mensagemCodificada);
      try {
        Thread.sleep(25);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
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

    return fluxoDeBits;
  }

  public static void MeioDeComunicacao(int fluxoBrutoDeBits[]) {
    int[] fluxoTransmissor, fluxoReceptor;

    fluxoTransmissor = fluxoBrutoDeBits;
    fluxoReceptor = new int[fluxoTransmissor.length];

    for (int i = 0; i < fluxoTransmissor.length; i++) {
      fluxoReceptor[i] = fluxoTransmissor[i];

      if (fila.size() < 43) {
        fila.addFirst(fluxoReceptor[i]);
      } else {
        fila.removeLast();
        fila.addFirst(fluxoReceptor[i]);
      }

      try {
        Thread.sleep(25);
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
            if (fila.get(k + 1) == 0) {
              if (fila.get(k) == 0) {
                janela.bits[k].setIcon(new ImageIcon("00.png"));
              } else {
                janela.bits[k].setIcon(new ImageIcon("10.png"));
              }
            } else {
              if (fila.get(k) == 0) {
                janela.bits[k].setIcon(new ImageIcon("01.png"));
              } else {
                janela.bits[k].setIcon(new ImageIcon("11.png"));
              }
            }
          }

        }
      }
    }
    for (int i = 0; i < 43; i++) {
      if (fila.size() < 43) {
        fila.addFirst(3);
      } else {
        fila.removeLast();
        fila.addFirst(3);
      }
    }
    for (int k = 0; k < fila.size(); k++) {
      janela.bits[k].setIcon(new ImageIcon("__.png"));
      try {
        Thread.sleep(25);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }

    CamadaFisicaReceptora(fluxoReceptor);
  }

  public static void CamadaFisicaReceptora(int fluxoBrutoDeBits[]) {

    String mensagem = "";
    for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
      mensagem += fluxoBrutoDeBits[i];
      janela.campoDeTextoDecodificado.setText(mensagem);
      try {
        Thread.sleep(25);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }

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

      contador++;
    }

    return bits;
  }

  public static void CamadaDeAplicacaoReceptora(int bits[]) {
    String aux = "";

    for (int i = 0; i < bits.length; i++) {

      aux = aux.concat("" + bits[i]);
      janela.campoDeTextoDecodificadoPalavra.setText(aux);
      try {
        Thread.sleep(25);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }

    String mensagem = "";
    for (int i = 0; i < bits.length / 8; i++) {

      mensagem = mensagem.concat("" + (char) Integer.parseInt(aux.substring(i * 8, (i * 8) + 8), 2));
    }

    AplicacaoReceptora(mensagem);
  }

  public static void AplicacaoReceptora(String mensagem) {
    janela.campoReceptora.setText("A mensagem recebida foi: " + mensagem);

  }
}