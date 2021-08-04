/* *********************************************************************************
* Autor: Lucas de Oliveira Alves Flores                                            *
* Matricula: 201911916                                                             *
* Inicio: 27 de julho de 2021                                    	                 *
* Ultima alteracao: 03 de agosto de 2021                                           *
* Funcao: Simular Tranmissao e Recepcao de Dados pela Camada Fisica                *
********************************************************************************** */

/* *********************************************************************************
* Classe: Principal                                                                *
* Funcao: Implementar os metodos que simulam as fases da tranmissao e recepcao     *
* dos dados pela camada fisica                                                     *
********************************************************************************** */

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

  public static void AplicacaoTransmissora() { // Inicio Aplicacao Transmissora
    try {
      janela.semaforo.acquire();
    } catch (InterruptedException ex) {
    }

    /*
     * Chamada da Camada de Aplicacao Transmissora apenas depois que a mensagem foi
     * digitada e o semaforo receber um release
     */
    CamadaDeAplicacaoTransmissora(janela.mensagem);
  } // Fim AplicacaoTransmissora

  public static void CamadaDeAplicacaoTransmissora(String mensagem) {// Inicio Camada de Aplicacao Transmissora

    // Conversao de cada Caractere da String mensagem para um array de String
    // contendo os respectivos Binarios
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

    /* Trecho de Codigo apenas para exibicao na Interface */
    String binario = "";
    for (int i = 0; i < quadro.length; i++) {
      binario += quadro[i];
      janela.campoDeTextoPalavra.setText(binario);
      try {
        Thread.sleep(30);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }

    // Conversao do Array de Strings de tamanho 8 para um Array (8 vezes maior) de
    // Int
    int[] bits = new int[quadro.length * 8];
    for (int i = 0; i < quadro.length; i++) {
      for (int k = 0; k < quadro[i].length(); k++) {
        bits[i * 8 + k] = Character.getNumericValue(quadro[i].charAt(k));
      }
    }

    // Chamada da CamadaFisicaTransmissora
    CamadaFisicaTransmissora(bits);
  } // Fim Camada de Aplicacao Transmissora

  public static void CamadaFisicaTransmissora(int bits[]) { // Inicio da Camada Fisica Transmissora
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

    /* Trecho de Codigo apenas para exibicao na Interface */
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

    // Chamada da MeioDeComunicacao
    MeioDeComunicacao(fluxoBrutoDeBits);
  } // Fim da Camada Fisica Transmissora

  public static int[] CamadaFisicaTransmissoraCodificacaoManchester(int bits[]) { // Inicio da Codificacao Manchester
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
  } // Fim da Codificacao Manchester

  // Inicio da Codificacao Manchester Diferencial
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
  } // Fim da Codificacao Manchester Diferencial

  public static void MeioDeComunicacao(int fluxoBrutoDeBits[]) { // Inicio do Meio de Comunicacao
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

    /* Trecho de Codigo apenas para exibicao na Interface */
    for (int k = 0; k < fila.size(); k++) {
      janela.bits[k].setIcon(new ImageIcon("__.png"));
      try {
        Thread.sleep(25);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }

    // Chamada da CamadaFisicaReceptora
    CamadaFisicaReceptora(fluxoReceptor);
  } // Fim do Meio de Comunicacao

  // Inicio da Camada Fisica Receptora
  public static void CamadaFisicaReceptora(int fluxoBrutoDeBits[]) {

    /* Trecho de Codigo apenas para exibicao na Interface */
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

    // Chamada CamadaAplicacaoReceptora
    CamadaDeAplicacaoReceptora(bits);
  } // Fim da Camada Fisica Receptora

  // Inicio da Camada Fisica Receptora de Decoficacao Manchester
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
  } // Fim da Camada Fisica Receptora de Decoficacao Manchester

  // Inicio da Camada Fisica Receptora de Decoficacao Manchester
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
  } // Fim da Camada Fisica Receptora de Decoficacao Manchester Diferencial

  // Inicio da Camada de Aplicacao Receptora
  public static void CamadaDeAplicacaoReceptora(int bits[]) {
    String aux = "";

    for (int i = 0; i < bits.length; i++) {
      /* Trecho de Codigo apenas para exibicao na Interface */
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

    // Chamada AplicacaoReceptora
    AplicacaoReceptora(mensagem);
  } // Fim da Camada de Aplicacao Receptora

  public static void AplicacaoReceptora(String mensagem) { // Inicio Aplicacao Receptora
    janela.campoReceptora.setText("A mensagem recebida foi: " + mensagem);
  }// Fim Aplicacao Receptora
}