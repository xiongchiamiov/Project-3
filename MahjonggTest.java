import org.uispec4j.*;
import org.uispec4j.interception.*;

public class MahjonggTest extends UISpecTestCase 
{

  //Need to call this to initialize UiSpec4J
  static    {
    UISpec4J.init();
  }

  Window window;

  //Intercept the window and store it off
  protected void setUp() throws Exception {
    window = WindowInterceptor.run(new Trigger() {
      public void run() {
        String[] args = {"Mahjongg"};
        GridGameLoader.main(args);          
      }
    });
  }

  private void pause()
  {
    try {
        Thread.sleep(200);
    } catch (InterruptedException ex) {}
  }
  
  public void testGamePlay()
  {
	// verify the board number is correct
    assertTrue("Title bar text incorrect.",window.titleContains("Mahjongg - board 1"));
    TextBox theLabel = window.getTextBox("Status");
    assertEquals("Status label text incorrect.","Tiles Left: 84", theLabel.getText().substring(0,14));
    // remove two tiles from the right edge  (top row)
    Table table = window.getTable();
    table.click(0,11);
    table.click(1,9);
    pause();
    // See if the tiles remaining label is decremented
    theLabel = window.getTextBox("Status");
    assertEquals("Tiles left not decremented.","Tiles Left: 82", theLabel.getText().substring(0,14));

    // remove two tiles from the left edge (bottom row)
    table.click(2,1);
    table.click(7,0);
    pause();
    // See if the tiles remaining label is decremented
    theLabel = window.getTextBox("Status");
    assertEquals("Removing left edge tiles fails.","Tiles Left: 80", theLabel.getText().substring(0,14));

    // remove one tile from opposite edges
    table.click(3,0);
    table.click(5,10);
    pause();
    // See if the tiles remaining label is decremented
    theLabel = window.getTextBox("Status");
    assertEquals("Removing opposite edge tiles fails.","Tiles Left: 78", theLabel.getText().substring(0,14));

    // These actions should NOT remove any tiles
    // clicking the same tile twice
    table.click(0,0);
    table.click(0,0);
    pause();
    theLabel = window.getTextBox("Status");
    assertEquals("Clicking same tile twice fails.","Tiles Left: 78", theLabel.getText().substring(0,14));

    // first matching tile available but second one not
    table.click(3,1);
    table.click(5,3);
    pause();
    theLabel = window.getTextBox("Status");
    assertEquals("Unavailable tile check fails.","Tiles Left: 78", theLabel.getText().substring(0,14));

    // first matching tile blocked but second one open
    table.click(3,4);
    table.click(6,9);
    pause();
    theLabel = window.getTextBox("Status");
    assertEquals("Blocked tile check fails.","Tiles Left: 78", theLabel.getText().substring(0,14));

    // both tiles available but don't match by suit, have same rank
    table.click(4,0);
    table.click(6,9);
    pause();
    theLabel = window.getTextBox("Status");
    assertEquals("Mismatched suits fail.","Tiles Left: 78", theLabel.getText().substring(0,14));

    // both tiles available with same suit, different rank
    table.click(4,0);
    table.click(3,1);
    pause();
    theLabel = window.getTextBox("Status");
    assertEquals("Mismatched ranks fail.","Tiles Left: 78", theLabel.getText().substring(0,14));

    window.getMenuBar().getMenu("Game").getSubMenu("New Game").click();  // go to game 2
    window.getMenuBar().getMenu("Game").getSubMenu("New Game").click();  // go to game 3
    assertTrue("new game fails.",window.titleContains("board 3"));
    pause();
    theLabel = window.getTextBox("Status");
    assertEquals("new game tile count incorrect.","Tiles Left: 84", theLabel.getText().substring(0,14));

   
    WindowInterceptor.init(window.getMenuBar().getMenu("Game").getSubMenu("Hint").triggerClick())
    .process(BasicHandler.init().assertContainsText("Hint: ").triggerButtonClick("OK")).run();
       
    // remove a matching pair
    table = window.getTable();
    table.click(0,0);
    table.click(4,0);
    pause();
    
    
    // See if the tiles remaining label is decremented
    theLabel = window.getTextBox("Status");
    assertEquals("Tiles Left: 82", theLabel.getText().substring(0,14));

    // Try the cheat option
    window.getMenuBar().getMenu("Game").getSubMenu("Cheat").click();     
    table = window.getTable();
    pause();
    theLabel = window.getTextBox("Status");
   
    assertEquals("Cheat tile count incorrect.","Tiles Left: 2", theLabel.getText().substring(0,13));
    table.click(3,5);  // click next to last tile
    // Click the last tile and capture the win message.
    WindowInterceptor.init(table.triggerClick(3,6,Key.Modifier.NONE))
    .process(BasicHandler.init().assertContainsText("You win!").triggerButtonClick("OK")).run();
     
    window.getMenuBar().getMenu("Game").getSubMenu("New Game").click();  // go to game 4
    assertTrue(window.titleContains("board 4"));
    
    table.click(3,11);
    table.click(6,2);
    pause();
    theLabel = window.getTextBox("Status");
    assertEquals("Tiles Left: 82", theLabel.getText().substring(0,14));
    window.getMenuBar().getMenu("Game").getSubMenu("Restart").click();  // restart game
    assertTrue("Restart fails.",window.titleContains("board 4"));
    // the board should be restarted   
    theLabel = window.getTextBox("Status");
    assertEquals("restart has wrong tile count","Tiles Left: 84", theLabel.getText().substring(0,14));
    
    // go to game seven and win
    window.getMenuBar().getMenu("Game").getSubMenu("New Game").click();  // go to game 5
    window.getMenuBar().getMenu("Game").getSubMenu("New Game").click();  // go to game 6
    window.getMenuBar().getMenu("Game").getSubMenu("New Game").click();  // go to game 7
    assertTrue(window.titleContains("board 7"));
    table.click(3,11);
    table.click(4,11);
    table.click(3,0);
    table.click(1,2);
    table.click(2,1);
    table.click(4,10);
    table.click(4,9);
    table.click(6,2);
    table.click(6,3);
    table.click(6,9);
    table.click(6,8);
    table.click(7,0);
    table.click(6,4);
    table.click(3,10);
    table.click(3,1);
    table.click(0,11);
    table.click(0,10);
    table.click(4,8);
    table.click(6,5);
    table.click(7,11);
    table.click(4,0);
    table.click(7,10);
    table.click(2,2);
    table.click(4,1);
    table.click(2,3);
    table.click(7,9);
    table.click(5,10);
    table.click(2,10);
    table.click(3,2);
    table.click(0,0);
    table.click(2,9);
    table.click(5,10);
    table.click(0,9);
    table.click(1,9);
    table.click(5,9);
    table.click(5,8);
    table.click(0,1);
    table.click(4,2);
    table.click(5,7);
    table.click(2,4);
    table.click(4,3);
    table.click(7,8);
    table.click(0,2);
    table.click(1,3);
    table.click(2,5);
    table.click(0,3);
    table.click(2,6);
    table.click(0,8);
    table.click(1,4);
    table.click(5,6);
    table.click(5,1);
    table.click(5,2);
    table.click(0,4);
    table.click(0,5);
    table.click(3,3);
    table.click(0,6);
    table.click(1,5);
    table.click(4,4);
    table.click(6,7);
    table.click(5,5);
    table.click(2,8);
    table.click(6,6);
    table.click(2,7);
    table.click(4,5);
    table.click(0,7);
    table.click(4,6);
    table.click(7,7);
    table.click(7,6);
    table.click(1,8);
    table.click(3,4);
    table.click(7,5);
    table.click(7,4);
    table.click(1,6);
    table.click(3,5);
    table.click(7,1);
    table.click(4,7);
    table.click(3,6);
    table.click(5,4);
    table.click(5,3);
    table.click(7,2);
    table.click(1,7);
    table.click(3,9);
    table.click(7,3);
    pause();
   
    theLabel = window.getTextBox("Status");
    //System.out.println("Status: " + theLabel.getText());
    assertEquals("winning a game fails.","Tiles Left: 2", theLabel.getText().substring(0,13));
table.click(3,7);
    // Click the last tile and capture the win message.
    WindowInterceptor.init(table.triggerClick(3,8,Key.Modifier.NONE))
    .process(BasicHandler.init().assertContainsText("You win!").triggerButtonClick("OK")).run();
    
    // go to game eight and lose
    window.getMenuBar().getMenu("Game").getSubMenu("New Game").click();  // go to game 8
    assertTrue(window.titleContains("board 8"));
    
    table.click(5,1);
    table.click(0,0);
    table.click(3,11);
    table.click(0,1);
    table.click(0,2);
    table.click(0,11);
    table.click(1,9);
    table.click(0,3);
    table.click(0,4);
    table.click(7,0);
    table.click(4,0);
    table.click(0,5);
    table.click(0,10);
    table.click(4,1);
    table.click(7,1);
    table.click(0,6);
    table.click(7,2);
    table.click(3,10);
    table.click(7,3);
    table.click(7,11);
    table.click(7,10);
    table.click(1,2);
    table.click(4,11);
    table.click(7,4);
    table.click(5,10);
    table.click(1,3);
    table.click(4,10);
    table.click(5,2);
    pause();
    
    WindowInterceptor.init(window.getMenuBar().getMenu("Game").getSubMenu("Hint").triggerClick())
    .process(BasicHandler.init().assertContainsText("No moves available.").triggerButtonClick("OK")).run();

    // don't use the Quit menu or the test will terminate

  }
}