import gridgame.GridBoard;
import gridgame.GridGUI;
import gridgame.GridGame;
import gridgame.GridStatus;

import java.lang.reflect.Constructor;

public class GridGameLoader
{
    public enum ReturnCode {
        SUCCESS,
        MISSING_PARAMETER,
        UNABLE_TO_FIND_CLASS,
        NO_CONSTRUCTOR_FOUND
    }

    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.err.println("Missing parameter: plugin name");
            System.exit(ReturnCode.MISSING_PARAMETER.ordinal());
        }
        
        String className = args[0];
        className = className.toLowerCase() + "." + className;
        //Class<GridGUI> gridGuiClass = null; // The Java compiler's not very smart.
        Class<GridGame> gridGameClass = null;
        Class<GridBoard> gridBoardClass = null;
        Class<GridStatus> gridStatusClass = null;
        try
        {
            //gridGuiClass = (Class<GridGUI>)Class.forName(className+"GUI");
            gridGameClass = (Class<GridGame>)Class.forName(className+"Game");
            gridBoardClass = (Class<GridBoard>)Class.forName(className+"Board");
            gridStatusClass = (Class<GridStatus>)Class.forName(className+"Status");
        }
        catch (ClassNotFoundException e)
        {
            // It could be one of a number of classes, so let's pull the name
            // out from the exception.
            className = e.toString().replace("java.lang.ClassNotFoundException: ",
                                             "");
            System.err.println("Unable to find a class named "+className
                              +" in the CLASSPATH.");
            System.exit(ReturnCode.UNABLE_TO_FIND_CLASS.ordinal());
        }

        GridGUI gridGUI = null;
        GridGame gridGame = null;
        GridBoard gridBoard = null;
        GridStatus gridStatus = null;
        try
        {
            gridStatus = gridStatusClass.newInstance();
            gridBoard = gridBoardClass.newInstance();
            Constructor<GridGame> gridGameConstructor = gridGameClass.getConstructor(
             new Class[] { GridBoard.class, GridStatus.class });
            gridGame = gridGameConstructor.newInstance(
             new Object[] { gridBoard, gridStatus });
            gridGUI = new GridGUI(args[0], gridGame);
        }
        catch (InstantiationException e) { e.printStackTrace(); }
        catch (IllegalAccessException e) { e.printStackTrace(); }
        catch (java.lang.reflect.InvocationTargetException e) {}
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
            System.err.println("No constructor found for "+className+"Game with a argument list of size 2.");
            System.exit(ReturnCode.NO_CONSTRUCTOR_FOUND.ordinal());
        }

        gridBoard.setParent(gridGame);
        gridGame.init();
        gridGame.addObserver(gridGUI);
        gridGUI.createUI();
        gridGUI.setVisible(true);
    }
}

