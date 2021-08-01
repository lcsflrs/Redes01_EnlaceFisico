import java.util.Scanner;

public class Principal {
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

    System.out.println("\n\n");
    for (int i = 0; i < bits.length; i++) {
      System.out.print(bits[i]);
    }

    CamadaFisicaTransmissora(bits);
  }

  public static void CamadaFisicaTransmissora(int bits[]) {
    int tipoDeCodificacao = 2;
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
    System.out.println("\n\nCodificacao Manchester :");
    for (int i = 0; i < fluxoDeBits.length; i++) {
      System.out.print(fluxoDeBits[i]);
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
    System.out.println("\n\nCodificacao Manchester Diferencial:");
    for (int i = 0; i < fluxoDeBits.length; i++) {
      System.out.print(fluxoDeBits[i]);
    }
    return fluxoDeBits;
  }

  public static void MeioDeComunicacao(int fluxoBrutoDeBits[]) {
    int[] fluxoTransmissor, fluxoReceptor;

    fluxoTransmissor = fluxoBrutoDeBits;
    fluxoReceptor = new int[fluxoTransmissor.length];

    System.out.println("\n\nFluxo Transmissor: ");
    for (int i = 0; i < fluxoTransmissor.length; i++) {
      System.out.print(fluxoTransmissor[i]);
    }

    for (int i = 0; i < fluxoTransmissor.length; i++) {
      fluxoReceptor[i] = fluxoTransmissor[i];
      // Gui
    }

    System.out.println("\nFluxo Receptor: ");
    for (int i = 0; i < fluxoReceptor.length; i++) {
      System.out.print(fluxoReceptor[i]);
    }

    CamadaFisicaReceptora(fluxoReceptor);
  }

  public static void CamadaFisicaReceptora(int fluxoBrutoDeBits[]) {
    int tipoDeCodificacao = 2;
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

    System.out.println("\n\nBits chegando na Camada Fisica Receptora: ");
    for (int i = 0; i < bits.length; i++) {
      System.out.print(bits[i]);
    }
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
}