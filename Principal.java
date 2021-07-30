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
      System.out.println("\n\nConverterAscii: " + converterAscii);
      int codigoAscii = (int) converterAscii;
      System.out.println("CodigoAscii: " + codigoAscii);
      String inserir = Integer.toBinaryString(codigoAscii);
      System.out.println("ToBinaryString: " + Integer.toBinaryString(codigoAscii));
      // System.out.println("ParseInt: " +
      // Integer.parseInt(Integer.toBinaryString(codigoAscii)));
      if (inserir.length() < 8) {
        System.out.println("\n");
        while (inserir.length() < 8) {
          System.out.println("Diferenca a 8 bits: " + (8 - inserir.length()));
          String zero = "0";
          inserir = zero.concat(inserir);
        }
        System.out.println("Saiu do IF \n");

      }
      System.out.println("ToBinaryString Com Zero: " + inserir);
      quadro[i] = inserir;
    }

    int[] bits = new int[quadro.length * 8];
    for (int i = 0; i < quadro.length; i++) {
      System.out.println("\n\nStringAtual: " + quadro[i]);
      for (int k = 0; k < quadro[i].length(); k++) {
        System.out.println("\n\nIndex: " + k);
        System.out.println("CharAt: " + quadro[i].charAt(k));
        System.out.println("Char Get Numeric: " + Character.getNumericValue(quadro[i].charAt(k)));
        bits[i * 8 + k] = Character.getNumericValue(quadro[i].charAt(k));
      }
    }

    System.out.println("\n\n");
    for (int i = 0; i < quadro.length; i++) {
      System.out.print(quadro[i]);
    }

    System.out.println("\n\n");
    for (int i = 0; i < bits.length; i++) {
      System.out.print(bits[i]);
    }

  }
}