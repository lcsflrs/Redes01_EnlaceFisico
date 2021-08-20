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

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;

public class Principal {

  public static int tipoDeCodificacao = 1;
  public static int tipoDeEnquadramento = 0;
  public static Janela janela = new Janela();
  public static LinkedList<Integer> fila = new LinkedList<Integer>();
  public static int quantidadeQuadros = 43;

  public static void main(String args[]) {
    AplicacaoTransmissora();
  }

  /*************/
  /* APLICACAO */
  /*************/

  public static void AplicacaoTransmissora() { // Inicio Aplicacao Transmissora
    try {
      janela.semaforo.acquire();
    } catch (InterruptedException ex) {
    }

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
    String mensagemCodificada = "";
    for (int i = 0; i < quadro.length; i++) {
      mensagemCodificada += quadro[i] + "  ";
      janela.campoDeTextoPalavra.setText(mensagemCodificada);
      try {
        Thread.sleep(5);
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

    // Chamada da CamadaEnlaceDadosTransmissora
    CamadaEnlaceDadosTransmissora(bits);
  } // Fim Camada de Aplicacao Transmissora

  /**********/
  /* ENLACE */
  /**********/

  public static void CamadaEnlaceDadosTransmissora(int[] quadro) {
    int quadroEnquadrado[];
    quadroEnquadrado = CamadaEnlaceDadosTransmissoraEnquadramento(quadro);

    // Chamada da CamadaFisicaTransmissora
    CamadaFisicaTransmissora(quadroEnquadrado);
  }

  public static int[] CamadaEnlaceDadosTransmissoraEnquadramento(int quadro[]) {
    int[] quadroEnquadrado = new int[quadro.length];
    // System.out.println("\nMensagem Chegando para Enquadramento:\n " + mensagem);

    System.out.println("\nMensagem Antes de Enquadrada: ");
    for (int i = 0; i < quadro.length; i++) {
      if (i % 8 == 0) {
        System.out.print(" ");
      }
      System.out.print(quadro[i]);
    }
    System.out.println("\n");

    switch (tipoDeEnquadramento) {
      case 0:
        quadroEnquadrado = CamadaDeEnlaceTransmissoraEnquadramentoContagemDeCaracteres(quadro);
        break;
      case 1:
        quadroEnquadrado = CamadaDeEnlaceTransmissoraEnquadramentoInsercaoDeBytes(quadro);
        break;
      case 2:
        quadroEnquadrado = CamadaDeEnlaceTransmissoraEnquadramentoInsercaoDeBits(quadro);
        break;
    }
    // System.out.println("\nMensagem Enquadrada: ");
    // for (int i = 0; i < quadroEnquadrado.length; i++) {
    // if (i % 8 == 0) {
    // System.out.print(" ");
    // }
    // System.out.print(quadroEnquadrado[i]);
    // }

    /* Trecho de Codigo apenas para exibicao na Interface */
    String mensagemCodificada = "";
    for (int i = 0; i < quadroEnquadrado.length; i++) {
      if (i % 8 == 0) {
        mensagemCodificada += "  ";
      }
      mensagemCodificada += quadroEnquadrado[i];
      janela.campoEnquadramentoTransmissora.setText(mensagemCodificada);
      try {
        Thread.sleep(5);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }

    return quadroEnquadrado;
  }

  public static int[] CamadaDeEnlaceTransmissoraEnquadramentoContagemDeCaracteres(int quadro[]) {
    // System.out.println("Entrou em Contagem de Caracteres\n");
    ArrayList<Integer> quadroEnquadrado = new ArrayList<Integer>();
    int counter = 0;
    while (counter < quadro.length) {
      // System.out.println("Entrou no while, counter = " + counter);
      // System.out.println("Quadro.length = " + quadro.length);
      if (quadro.length >= counter + 32) {
        String inserir = Integer.toBinaryString(5);
        // System.out.println("Inserir Antes: " + inserir);
        if (inserir.length() < 8) {
          while (inserir.length() < 8) {
            String zero = "0";
            inserir = zero.concat(inserir);
          }
        }
        // System.out.println("Inserir Depois: " + inserir);

        // inserindo os bits do byte de contagem no array
        // System.out.println("\nInserindo o Inserir: ");
        for (int i = 0; i < inserir.length(); i++) {
          // System.out.print(Integer.parseInt(inserir.charAt(i) + ""));
          quadroEnquadrado.add(Integer.parseInt(inserir.charAt(i) + ""));
        }

        /*
         * System.out.println("Inserido em Quadro Enquadrado: "); for (int i = 0; i <
         * quadroEnquadrado.size(); i++) {
         * System.out.println("Inserido em Quadro Enquadrado: " +
         * quadroEnquadrado.get(i)); }
         */

        // System.out.println("\n\nInserindo os demais: ");
        for (int i = counter; i < counter + 32; i++) {
          /*
           * if ((i + 1) % 8 == 0) { System.out.print("  "); }
           */
          quadroEnquadrado.add(quadro[i]);
        }

        /*
         * for (int i = 0; i < quadroEnquadrado.size(); i++) {
         * System.out.println("Inserido em Quadro Enquadrado: " +
         * quadroEnquadrado.get(i)); }
         */

        // System.out.println("\n");
        counter += 32;
      } else {
        // System.out.println("Entrou no else");
        String inserir = Integer.toBinaryString(((quadro.length - counter + 8) / 8));
        if (inserir.length() < 8) {
          while (inserir.length() < 8) {
            String zero = "0";
            inserir = zero.concat(inserir);
          }
        }
        // System.out.println("Veio pro Else, inserir = " + inserir);
        // System.out.println("Tamanho do Quadro: " + quadro.length);
        // System.out.println("Tamanho do Counter: " + counter);
        // inserindo os bits do byte de contagem no array
        for (int i = 0; i < inserir.length(); i++) {
          quadroEnquadrado.add(Integer.parseInt(inserir.charAt(i) + ""));
        }
        for (int i = counter; i < quadro.length; i++) {
          quadroEnquadrado.add(quadro[i]);
        }
        counter += 32;
      }
    }

    Integer[] _quadrosAux = new Integer[quadroEnquadrado.size()];
    _quadrosAux = quadroEnquadrado.toArray(_quadrosAux);

    int[] _quadros = new int[_quadrosAux.length];
    for (int i = 0; i < _quadrosAux.length; i++) {
      _quadros[i] = _quadrosAux[i].intValue();
    }

    return _quadros;
  }

  public static int[] CamadaDeEnlaceTransmissoraEnquadramentoInsercaoDeBytes(int quadro[]) {
    ArrayList<Integer> quadroEnquadrado = new ArrayList<Integer>();
    int counter = 0;
    String flag = Integer.toBinaryString('F');
    String escape = Integer.toBinaryString('E');

    if (flag.length() < 8) {
      while (flag.length() < 8) {
        String zero = "0";
        flag = zero.concat(flag);
      }
    }

    if (escape.length() < 8) {
      while (escape.length() < 8) {
        String zero = "0";
        escape = zero.concat(escape);
      }
    }

    // System.out.print("Flag: ");
    // for (int i = 0; i < flag.length(); i++) {
    // System.out.print(flag.charAt(i));
    // }

    // System.out.print("\nEscape: ");
    // for (int i = 0; i < escape.length(); i++) {
    // System.out.print(escape.charAt(i));
    // }

    while (counter < quadro.length) {
      // System.out.println("\n====================\nwhile, counter = " + counter);
      for (int i = 0; i < flag.length(); i++) {
        // System.out.print(Integer.parseInt(flag.charAt(i) + ""));
        quadroEnquadrado.add(Integer.parseInt(flag.charAt(i) + ""));
      }
      if (quadro.length >= counter + 32) {
        for (int k = 0; k < 4; k++) {
          String caracter = "";
          for (int i = counter + (8 * k); i < counter + 8 + (8 * k); i++) {
            caracter = caracter + quadro[i];
          }
          // System.out.println("Caracter atual: " + caracter);
          if (caracter.equals(flag)) {
            // System.out.println("Eh igual ao flag: " + flag);
            for (int i = 0; i < escape.length(); i++) {
              // System.out.print(Integer.parseInt(escape.charAt(i) + ""));
              quadroEnquadrado.add(Integer.parseInt(escape.charAt(i) + ""));
            }
          } else if (caracter.equals(escape)) {
            // System.out.println("Eh igual ao escape: " + escape);
            for (int i = 0; i < escape.length(); i++) {
              // System.out.print(Integer.parseInt(escape.charAt(i) + ""));
              quadroEnquadrado.add(Integer.parseInt(escape.charAt(i) + ""));
            }
          }
          for (int i = 0; i < caracter.length(); i++) {
            // System.out.print(Integer.parseInt(caracter.charAt(i) + ""));
            quadroEnquadrado.add(Integer.parseInt(caracter.charAt(i) + ""));
          }
          caracter = "";
        }
        counter += 32;
      } else {
        for (int k = 0; k < (quadro.length - counter) / 8; k++) {
          String caracter = "";
          for (int i = counter + (8 * k); i < counter + 8 + (8 * k); i++) {
            caracter = caracter + quadro[i];
          }
          // System.out.println("Caracter atual: " + caracter);
          if (caracter.equals(flag)) {
            // System.out.println("Eh igual ao flag: " + flag);
            for (int i = 0; i < escape.length(); i++) {
              // System.out.print(Integer.parseInt(escape.charAt(i) + ""));
              quadroEnquadrado.add(Integer.parseInt(escape.charAt(i) + ""));
            }
          } else if (caracter.equals(escape)) {
            // System.out.println("Eh igual ao escape: " + escape);
            for (int i = 0; i < escape.length(); i++) {
              // System.out.print(Integer.parseInt(escape.charAt(i) + ""));
              quadroEnquadrado.add(Integer.parseInt(escape.charAt(i) + ""));
            }
          }
          for (int i = 0; i < caracter.length(); i++) {
            // System.out.print(Integer.parseInt(caracter.charAt(i) + ""));
            quadroEnquadrado.add(Integer.parseInt(caracter.charAt(i) + ""));
          }
          caracter = "";
        }

        counter += 32;
      }

      for (int i = 0; i < flag.length(); i++) {
        // System.out.print(Integer.parseInt(flag.charAt(i) + ""));
        quadroEnquadrado.add(Integer.parseInt(flag.charAt(i) + ""));
      }

      // System.out.println("Array final do While: ");
      // for (int i = 0; i < quadroEnquadrado.size(); i++) {
      // if (i % 8 == 0) {
      // System.out.print(" ");
      // }
      // System.out.print(quadroEnquadrado.get(i));
      // }
    }

    Integer[] _quadrosAux = new Integer[quadroEnquadrado.size()];
    _quadrosAux = quadroEnquadrado.toArray(_quadrosAux);

    int[] _quadros = new int[_quadrosAux.length];
    for (int i = 0; i < _quadrosAux.length; i++) {
      _quadros[i] = _quadrosAux[i].intValue();
    }

    return _quadros;

  }

  public static int[] CamadaDeEnlaceTransmissoraEnquadramentoInsercaoDeBits(int quadro[]) {
    ArrayList<Integer> quadroEnquadrado = new ArrayList<Integer>();
    int counter = 0;
    while (counter < quadro.length) {
      if (quadro.length >= counter + 32) {
        String inserir = Integer.toBinaryString('~');
        System.out.println("Inserir Antes: " + inserir);
        if (inserir.length() < 8) {
          while (inserir.length() < 8) {
            String zero = "0";
            inserir = zero.concat(inserir);
          }
        }
        System.out.println("Inserir Depois: " + inserir);

        for (int i = 0; i < inserir.length(); i++) {
          quadroEnquadrado.add(Integer.parseInt(inserir.charAt(i) + ""));
        }

        int quantidadeUns = 0;
        for (int i = counter; i < counter + 32; i++) {
          if (quantidadeUns == 5) {
            quadroEnquadrado.add(0);
          }
          if (quadro[i] == 1) {
            quantidadeUns++;
          } else {
            quantidadeUns = 0;
          }
          quadroEnquadrado.add(quadro[i]);
        }
        for (int i = 0; i < inserir.length(); i++) {
          quadroEnquadrado.add(Integer.parseInt(inserir.charAt(i) + ""));
        }
        counter += 32;
      } else {
        String inserir = Integer.toBinaryString('~');
        if (inserir.length() < 8) {
          while (inserir.length() < 8) {
            String zero = "0";
            inserir = zero.concat(inserir);
          }
        }
        for (int i = 0; i < inserir.length(); i++) {
          quadroEnquadrado.add(Integer.parseInt(inserir.charAt(i) + ""));
        }
        int quantidadeUns = 0;
        for (int i = counter; i < quadro.length; i++) {
          if (quantidadeUns == 5) {
            quadroEnquadrado.add(0);
          }
          if (quadro[i] == 1) {
            quantidadeUns++;
          } else {
            quantidadeUns = 0;
          }
          quadroEnquadrado.add(quadro[i]);
        }
        for (int i = 0; i < inserir.length(); i++) {
          quadroEnquadrado.add(Integer.parseInt(inserir.charAt(i) + ""));
        }
        counter += 32;
      }
    }

    Integer[] _quadrosAux = new Integer[quadroEnquadrado.size()];
    _quadrosAux = quadroEnquadrado.toArray(_quadrosAux);

    int[] _quadros = new int[_quadrosAux.length];
    for (int i = 0; i < _quadrosAux.length; i++) {
      _quadros[i] = _quadrosAux[i].intValue();
    }

    return _quadros;
  }

  /**********/
  /* FISICA */
  /**********/

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
      if (i % 8 == 0) {
        mensagemCodificada += "  ";
      }
      mensagemCodificada += fluxoBrutoDeBits[i];
      janela.campoDeTextoCodificado.setText(mensagemCodificada);
      try {
        Thread.sleep(5);
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

  /***********************/
  /* MEIO DE COMUNICACAO */
  /***********************/

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
        Thread.sleep(5);
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
        Thread.sleep(5);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }

    // Chamada da CamadaFisicaReceptora
    CamadaFisicaReceptora(fluxoReceptor);
  } // Fim do Meio de Comunicacao

  /**********/
  /* FISICA */
  /**********/

  // Inicio da Camada Fisica Receptora
  public static void CamadaFisicaReceptora(int fluxoBrutoDeBits[]) {

    /* Trecho de Codigo apenas para exibicao na Interface */
    String mensagem = "";
    for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
      if (i % 8 == 0) {
        mensagem += "  ";
      }
      mensagem += fluxoBrutoDeBits[i];
      janela.campoDeTextoDecodificado.setText(mensagem);
      try {
        Thread.sleep(5);
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
    CamadaEnlaceDadosReceptora(bits);
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

  /**********/
  /* ENLACE */
  /**********/

  public static void CamadaEnlaceDadosReceptora(int[] quadros) {
    CamadaEnlaceDadosReceptoraEnquadramento(quadros);
  }

  public static void CamadaEnlaceDadosReceptoraEnquadramento(int[] quadros) {
    int[] quadrosDesenquadrados = new int[0];

    switch (tipoDeEnquadramento) {
      case 0:
        quadrosDesenquadrados = CamadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres(quadros);
        break;
      case 1:
        quadrosDesenquadrados = CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBytes(quadros);
        break;
      case 2:
        quadrosDesenquadrados = CamadaDeEnlaceReceptoraEnquadramentoInsercaoDeBits(quadros);
        break;
    }

    // System.out.println("\nMensagem Desenquadrada: ");
    // for (int i = 0; i < quadrosDesenquadrados.length; i++) {
    // if ((i) % 8 == 0) {
    // System.out.print(" ");
    // }
    // System.out.print(quadrosDesenquadrados[i]);
    // }

    /* Trecho de Codigo apenas para exibicao na Interface */
    String mensagemCodificada = "";
    for (int i = 0; i < quadros.length; i++) {
      if (i % 8 == 0) {
        mensagemCodificada += "  ";
      }
      mensagemCodificada += quadros[i];
      janela.campoEnquadramentoReceptora.setText(mensagemCodificada);
      try {
        Thread.sleep(5);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }

    CamadaDeAplicacaoReceptora(quadrosDesenquadrados);
  }

  public static int[] CamadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres(int[] quadros) {
    ArrayList<Integer> quadrosDesenquadrado = new ArrayList<Integer>();

    System.out.println("\n\nQuadros na Recepcao Contagem de Caracteres");
    for (int i = 0; i < quadros.length; i++) {
      if ((i) % 8 == 0) {
        System.out.print(" ");
      }
      System.out.print(quadros[i]);
    }

    String caracterDeContagem = "";
    int contador = 0;

    while (contador < quadros.length) {
      System.out.println("\n================================\nEntrou no While: contador = " + contador + "\n");
      if (caracterDeContagem == "") {
        System.out.println("\nCaracter de Contagem Vazio");
        for (int i = contador; i < contador + 8; i++) {
          caracterDeContagem = caracterDeContagem + quadros[i];
        }
        System.out.println("Caracter de Contagem: " + caracterDeContagem);
        contador += 8;
      } else {
        System.out.println("\nInteiro de Contagem: " + Integer.parseInt(caracterDeContagem, 2));
        System.out.println("Percorrendo de contador = " + contador + " ate: "
            + (contador + ((Integer.parseInt(caracterDeContagem, 2)) * 8)));
        for (int i = contador; i < (contador + ((Integer.parseInt(caracterDeContagem, 2) - 1) * 8)); i++) {
          System.out.print(i + " ");
          quadrosDesenquadrado.add(quadros[i]);
        }
        contador = contador + ((Integer.parseInt(caracterDeContagem, 2) - 1) * 8);
        caracterDeContagem = "";
      }
      System.out.println("\n\nArray ate agora:");
      for (int i = 0; i < quadrosDesenquadrado.size(); i++) {
        if (i % 8 == 0) {
          System.out.print(" ");
        }
        System.out.print(quadrosDesenquadrado.get(i));
      }
    }

    System.out.println("\n\nArray:");

    Integer[] _quadrosAux = new Integer[quadrosDesenquadrado.size()];
    _quadrosAux = quadrosDesenquadrado.toArray(_quadrosAux);

    int[] _quadros = new int[_quadrosAux.length];
    for (int i = 0; i < _quadrosAux.length; i++) {
      if (i % 8 == 0) {
        System.out.print(" ");
      }
      _quadros[i] = _quadrosAux[i].intValue();
      System.out.print(_quadros[i]);
    }

    return _quadros;
  }

  public static int[] CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBytes(int[] quadros) {
    ArrayList<Integer> quadrosDesenquadrado = new ArrayList<Integer>();

    String flag = Integer.toBinaryString('F');
    String escape = Integer.toBinaryString('E');

    if (flag.length() < 8) {
      while (flag.length() < 8) {
        String zero = "0";
        flag = zero.concat(flag);
      }
    }

    if (escape.length() < 8) {
      while (escape.length() < 8) {
        String zero = "0";
        escape = zero.concat(escape);
      }
    }

    System.out.println("\n\nQuadros na Recepcao Enquadramento Insercao de Bytes:");
    for (int i = 0; i < quadros.length; i++) {
      if ((i) % 8 == 0) {
        System.out.print(" ");
      }
      System.out.print(quadros[i]);
    }

    int counter = 0;
    boolean encontrouEscape = false;
    while (counter < quadros.length) {
      for (int k = 0; k < quadros.length / 8; k++) {
        String caracter = "";
        for (int i = (8 * k); i < 8 + (8 * k); i++) {
          caracter = caracter + quadros[i];
        }
        if (caracter.equals(escape) && !encontrouEscape) {
          encontrouEscape = true;
        } else if (caracter.equals(escape) && encontrouEscape) {
          for (int i = 0; i < escape.length(); i++) {
            quadrosDesenquadrado.add(Integer.parseInt(escape.charAt(i) + ""));
          }
          encontrouEscape = false;
        } else if (caracter.equals(flag) && encontrouEscape) {
          for (int i = 0; i < flag.length(); i++) {
            quadrosDesenquadrado.add(Integer.parseInt(flag.charAt(i) + ""));
          }
          encontrouEscape = false;
        } else if (!caracter.equals(flag)) {
          for (int i = 0; i < caracter.length(); i++) {
            quadrosDesenquadrado.add(Integer.parseInt(caracter.charAt(i) + ""));
          }
        }

        counter += 8;
      }
    }

    Integer[] _quadrosAux = new Integer[quadrosDesenquadrado.size()];
    _quadrosAux = quadrosDesenquadrado.toArray(_quadrosAux);

    int[] _quadros = new int[_quadrosAux.length];
    for (int i = 0; i < _quadrosAux.length; i++) {
      _quadros[i] = _quadrosAux[i].intValue();
    }

    return _quadros;
  }

  public static int[] CamadaDeEnlaceReceptoraEnquadramentoInsercaoDeBits(int quadros[]) {
    ArrayList<Integer> quadrosDesenquadrado = new ArrayList<Integer>();

    String flag = Integer.toBinaryString('~');
    if (flag.length() < 8) {
      while (flag.length() < 8) {
        String zero = "0";
        flag = zero.concat(flag);
      }
    }

    /*
     * System.out.println("\n\nQuadros na Recepcao Enquadramento Insercao de Bytes:"
     * ); for (int i = 0; i < quadros.length; i++) { if ((i) % 8 == 0) {
     * System.out.print(" "); } System.out.print(quadros[i]); }
     */

    String caracter = "";
    int quantidadeUns = 0;
    boolean encontrouFlag = false;
    int indexInicio = 0, indexFinal = 0;
    System.out.println("\n\n");
    for (int i = 0; i < quadros.length; i++) {
      // System.out
      // .println("Entrou no for,\ti = " + i + "\tquantidadeUns = " + quantidadeUns +
      // "\tletraAtual = " + quadros[i]);
      if (quantidadeUns == 6 && quadros[i] == 0) {
        if (!encontrouFlag) {
          // System.out.println("\nEncontrou Flag - Index inicio = + " + (i + 1) + "\n");
          encontrouFlag = true;
          indexInicio = i + 1;
        } else {
          // System.out.println("\nEncontrou Flag - Index final = + " + (i) + "\n");
          encontrouFlag = false;
          indexFinal = i - 8 + 1;
          for (int k = indexInicio; k < indexFinal; k++) {
            caracter = caracter + quadros[k];
          }
        }
        quantidadeUns = 0;
      } else if (quadros[i] == 1) {
        quantidadeUns++;
      } else {
        quantidadeUns = 0;
      }
    }

    // System.out.println("\nCaracter = " + caracter);

    for (int i = 0; i < caracter.length(); i++) {
      // System.out.println("For -> i = " + i + " char = " + caracter.charAt(i) + "
      // quantidade uns = " + quantidadeUns);
      if (quantidadeUns == 5 && caracter.charAt(i) == '0') {
        // System.out.println("Zero Removido");
        quantidadeUns = 0;
      } else if (caracter.charAt(i) == '1') {
        quantidadeUns++;
        quadrosDesenquadrado.add(Integer.parseInt(caracter.charAt(i) + ""));
      } else {
        quadrosDesenquadrado.add(Integer.parseInt(caracter.charAt(i) + ""));
        quantidadeUns = 0;
      }
    }

    Integer[] _quadrosAux = new Integer[quadrosDesenquadrado.size()];
    _quadrosAux = quadrosDesenquadrado.toArray(_quadrosAux);

    int[] _quadros = new int[_quadrosAux.length];
    for (int i = 0; i < _quadrosAux.length; i++) {
      _quadros[i] = _quadrosAux[i].intValue();
    }

    return _quadros;
  }

  /*************/
  /* APLICACAO */
  /*************/

  // Inicio da Camada de Aplicacao Receptora
  public static void CamadaDeAplicacaoReceptora(int bits[]) {
    String aux = "";

    String exibir = "";

    for (int i = 0; i < bits.length; i++) {
      /* Trecho de Codigo apenas para exibicao na Interface */
      if (i % 8 == 0) {
        exibir = exibir.concat("  ");
      }
      exibir = exibir.concat("" + bits[i]);
      aux = aux.concat("" + bits[i]);
      janela.campoDeTextoDecodificadoPalavra.setText(exibir);
      try {
        Thread.sleep(5);
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