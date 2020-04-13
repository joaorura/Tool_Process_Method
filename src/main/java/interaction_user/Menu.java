package interaction_user;

import change_method.JsonChangeMethodName;
import com.github.javaparser.StaticJavaParser;
import get_methods.JsonGetMethods;
import get_methods.PathFilterGetMethods;
import incomplete_code.JsonHashIncompleteCode;
import incomplete_code.PathIncomplete;
import repositories.CreateRepositorie;
import repositories.PathCreateRepositories;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class Menu {
    public static void menu(String[] args) {
        StaticJavaParser.getConfiguration().setAttributeComments(false);
        int type = parseInt(args[0]);

        InterfaceProcess interfaceProcess = null;
        args = Arrays.copyOfRange(args, 1, args.length);

        try {
            switch (type) {
                case 0:
                    interfaceProcess = new JsonGetMethods(args);
                    break;
                case 1:
                    interfaceProcess = new JsonChangeMethodName(args);
                    break;
                case 2:
                    interfaceProcess = new CreateRepositorie(args);
                    break;
                case 3:
                    interfaceProcess = new JsonHashIncompleteCode(args);
                    break;
                case 4:
                    interfaceProcess = new PathFilterGetMethods(args);
                    break;
                case 5:
                    interfaceProcess = new PathIncomplete(args);
                    break;
                case 6:
                    interfaceProcess = new PathCreateRepositories(args);
                    break;
                default:
                    System.out.println("Error in type described, must be 0 in 5");
                    break;
            }
        }
        catch ( IndexOutOfBoundsException e) {
            System.out.println("Error in args of program.");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }


        if(interfaceProcess == null) {
            System.out.println("Problem in Execution");
        }
        else {
            interfaceProcess.process();
        }

        System.out.println("End of Exectuion");
    }
}
