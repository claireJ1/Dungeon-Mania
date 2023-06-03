package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.entity.movingEntity.Assassin;
import dungeonmania.entity.movingEntity.Mercenary;
import dungeonmania.entity.movingEntity.Spider;
import dungeonmania.response.models.*;

@TestMethodOrder(OrderAnnotation.class)
public class ControllerTest {

        @BeforeEach
        public void setup() {
                Assassin.idNum = 1;
                Mercenary.idNum = 1;
                Spider.idNum = 1;
        }

        public static List<ItemResponse> fetchItemsByType(DungeonResponse resD, String type) {
                List<ItemResponse> ret = new ArrayList<>();
                for (ItemResponse e: resD.getInventory()) if (e.getType().equals(type)) ret.add(e);
                return ret;
        }

        @Test
        @Order(1)
        public void testInvalidGameMode() {
                DungeonManiaController controller = new DungeonManiaController();
                assertThrows(IllegalArgumentException.class, () -> controller.newGame("gameModeTest", "invalid"));
        }

        @Test
        @Order(2)
        public void testInvalidDungeon() {
                DungeonManiaController controller = new DungeonManiaController();
                assertThrows(IllegalArgumentException.class, () -> controller.newGame("invalid", "hard"));
        }

        @Test
        @Order(3)
        public void testInvalidLoadGame() {
                DungeonManiaController controller = new DungeonManiaController();
                assertThrows(IllegalArgumentException.class, () -> controller.loadGame("invalidname"));
        }

        @Test
        @Order(4)
        public void testValidNewGame() {
                DungeonManiaController controller = new DungeonManiaController();
                DungeonResponse info = controller.newGame("gameModeTest", "standard");
                List<EntityResponse> entities = info.getEntities();
                // spider (2, 2)
                assertTrue(entities.stream().filter(e -> e.getType().equals("spider"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("spider"))
                                .anyMatch(e -> e.getPosition().getY() == 2));
                // zombie_toast_spawner (4, 4)
                assertTrue(entities.stream().filter(e -> e.getType().equals("zombie_toast_spawner"))
                                .anyMatch(e -> e.getPosition().getX() == 4));
                assertTrue(entities.stream().filter(e -> e.getType().equals("zombie_toast_spawner"))
                                .anyMatch(e -> e.getPosition().getX() == 4));
                // player (0, 0)
                assertTrue(entities.stream().filter(e -> e.getType().equals("player"))
                                .anyMatch(e -> e.getPosition().getX() == 0));
                assertTrue(entities.stream().filter(e -> e.getType().equals("player"))
                                .anyMatch(e -> e.getPosition().getX() == 0));
        }

        @Test
        @Order(5)
        public void testSaveGame() {
                DungeonManiaController controller = new DungeonManiaController();
                DungeonResponse info = controller.newGame("gameModeTest", "standard");
                info = controller.saveGame("save_1");
                List<EntityResponse> entities = info.getEntities();
                // spider (2, 2)
                assertTrue(entities.stream().filter(e -> e.getType().equals("spider"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("spider"))
                                .anyMatch(e -> e.getPosition().getY() == 2));
                // zombie_toast_spawner (4, 4)
                assertTrue(entities.stream().filter(e -> e.getType().equals("zombie_toast_spawner"))
                                .anyMatch(e -> e.getPosition().getX() == 4));
                assertTrue(entities.stream().filter(e -> e.getType().equals("zombie_toast_spawner"))
                                .anyMatch(e -> e.getPosition().getX() == 4));
                // player (0, 0)
                assertTrue(entities.stream().filter(e -> e.getType().equals("player"))
                                .anyMatch(e -> e.getPosition().getX() == 0));
                assertTrue(entities.stream().filter(e -> e.getType().equals("player"))
                                .anyMatch(e -> e.getPosition().getX() == 0));
        }

        @Test
        @Order(6)
        public void testLoadGame() {
                DungeonManiaController controller = new DungeonManiaController();
                controller.newGame("gameModeTest", "standard");
                controller.tick(null, Direction.NONE);
                controller.saveGame("save2");
                DungeonManiaController controller2 = new DungeonManiaController();
                DungeonResponse info = controller2.loadGame("save2");
                List<EntityResponse> entities = info.getEntities();
                // spider (2,1)
                assertTrue(entities.stream().filter(e -> e.getType().equals("spider"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("spider"))
                                .anyMatch(e -> e.getPosition().getY() == 1));
                // zombie_toast_spawner (4, 4)
                assertTrue(entities.stream().filter(e -> e.getType().equals("zombie_toast_spawner"))
                                .anyMatch(e -> e.getPosition().getX() == 4));
                assertTrue(entities.stream().filter(e -> e.getType().equals("zombie_toast_spawner"))
                                .anyMatch(e -> e.getPosition().getY() == 4));
                // player (0, 0)
                assertTrue(entities.stream().filter(e -> e.getType().equals("player"))
                                .anyMatch(e -> e.getPosition().getX() == 0));
                assertTrue(entities.stream().filter(e -> e.getType().equals("player"))
                                .anyMatch(e -> e.getPosition().getY() == 0));

                assertThrows(IllegalArgumentException.class, () -> controller.loadGame("save_2"));
        }

        @Test
        @Order(7)
        public void testLoadGame2() {
                DungeonManiaController controller = new DungeonManiaController();
                controller.newGame("savetest", "standard");
                controller.tick(null, Direction.DOWN);
                controller.saveGame("save4");
                DungeonManiaController controller2 = new DungeonManiaController();
                DungeonResponse info = controller2.loadGame("save4");
                List<EntityResponse> entities = info.getEntities();
                // spider (2,1)
                assertTrue(entities.stream().filter(e -> e.getType().equals("boulder"))
                                .anyMatch(e -> e.getPosition().getX() == 1));
                assertTrue(entities.stream().filter(e -> e.getType().equals("boulder"))
                                .anyMatch(e -> e.getPosition().getY() == 3));
                // zombie_toast_spawner (4, 4)
                assertTrue(entities.stream().filter(e -> e.getType().equals("switch"))
                                .anyMatch(e -> e.getPosition().getX() == 1));
                assertTrue(entities.stream().filter(e -> e.getType().equals("switch"))
                                .anyMatch(e -> e.getPosition().getY() == 3));
                // player (0, 0)
                assertTrue(entities.stream().filter(e -> e.getType().equals("player"))
                                .anyMatch(e -> e.getPosition().getX() == 1));
                assertTrue(entities.stream().filter(e -> e.getType().equals("player"))
                                .anyMatch(e -> e.getPosition().getY() == 2));

                assertThrows(IllegalArgumentException.class, () -> controller.loadGame("invalid"));
        }

        @Test
        @Order(8)
        public void testAllGames() {
                DungeonManiaController controller = new DungeonManiaController();
                assertDoesNotThrow(()->controller.loadGame("save_1"));
                assertDoesNotThrow(()->controller.loadGame("save4"));
                assertDoesNotThrow(()->controller.loadGame("save2"));
        }

        @Test
        @Order(9)
        public void testGameWithAllEntities() {
                DungeonManiaController controller = new DungeonManiaController();
                DungeonResponse resD = controller.newGame("allEntity", "standard");
                for(int i = 0; i < 21; i++) {
                       resD = controller.tick(null, Direction.DOWN);
                }
                resD = controller.build("bow");
                resD = controller.build("shield");
                resD = controller.build("sceptre");
                List<ItemResponse> bomb = fetchItemsByType(resD,"bomb");
                controller.tick(bomb.get(0).getId(), null);
                resD = controller.tick(null, Direction.DOWN);
                
                DungeonResponse res1 = controller.saveGame("all");
                DungeonManiaController controller2 = new DungeonManiaController();
                DungeonResponse res2 = controller2.loadGame("all");
                List<EntityResponse> entities = res2.getEntities();

                assertTrue(entities.stream().filter(e -> e.getType().equals("player"))
                                .anyMatch(e -> e.getPosition().getX() == 1));
                assertTrue(entities.stream().filter(e -> e.getType().equals("player"))
                                .anyMatch(e -> e.getPosition().getY() == 24));

                assertTrue(entities.stream().filter(e -> e.getType().equals("boulder"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("boulder"))
                                .anyMatch(e -> e.getPosition().getY() == 1));

                assertTrue(entities.stream().filter(e -> e.getType().equals("door"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("door"))
                                .anyMatch(e -> e.getPosition().getY() == 2));
                
                assertTrue(entities.stream().filter(e -> e.getType().equals("exit"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("exit"))
                                .anyMatch(e -> e.getPosition().getY() == 3));

                assertTrue(entities.stream().filter(e -> e.getType().equals("switch"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("switch"))
                                .anyMatch(e -> e.getPosition().getY() == 4));

                assertTrue(entities.stream().filter(e -> e.getType().equals("light_bulb_off"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("light_bulb_off"))
                                .anyMatch(e -> e.getPosition().getY() == 5));

                assertTrue(entities.stream().filter(e -> e.getType().equals("switch_door"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("switch_door"))
                                .anyMatch(e -> e.getPosition().getY() == 6));
                
                assertTrue(entities.stream().filter(e -> e.getType().equals("portal"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("portal"))
                                .anyMatch(e -> e.getPosition().getY() == 7));

                assertTrue(entities.stream().filter(e -> e.getType().equals("swamp_tile"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("swamp_tile"))
                                .anyMatch(e -> e.getPosition().getY() == 8));

                assertTrue(entities.stream().filter(e -> e.getType().equals("wire"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("wire"))
                                .anyMatch(e -> e.getPosition().getY() == 9));

                assertTrue(entities.stream().filter(e -> e.getType().equals("portal"))
                                .anyMatch(e -> e.getPosition().getX() == 2));
                assertTrue(entities.stream().filter(e -> e.getType().equals("portal"))
                                .anyMatch(e -> e.getPosition().getY() == 10));

                assertTrue(entities.stream().filter(e -> e.getType().equals("zombie_toast_spawner"))
                                .anyMatch(e -> e.getPosition().getX() == 4));
                assertTrue(entities.stream().filter(e -> e.getType().equals("zombie_toast_spawner"))
                                .anyMatch(e -> e.getPosition().getY() == 1));

                assertTrue(entities.stream().filter(e -> e.getId().equals("assassin_1"))
                                .anyMatch(e -> e.getPosition().getX() == 4));
                assertTrue(entities.stream().filter(e -> e.getId().equals("assassin_1"))
                                .anyMatch(e -> e.getPosition().getY() == 2));
                
                assertTrue(entities.stream().filter(e -> e.getId().equals("mercenary_1"))
                                .anyMatch(e -> e.getPosition().getX() == 4));
                assertTrue(entities.stream().filter(e -> e.getId().equals("mercenary_1"))
                                .anyMatch(e -> e.getPosition().getY() == 3));

                assertTrue(entities.stream().filter(e -> e.getType().equals("hydra"))
                                .anyMatch(e -> e.getPosition().getX() == 4));
                assertTrue(entities.stream().filter(e -> e.getType().equals("hydra"))
                                .anyMatch(e -> e.getPosition().getY() == 5));

                assertTrue(entities.stream().filter(e -> e.getId().equals("zombie_toast_1"))
                                .anyMatch(e -> e.getPosition().getX() == 4));
                assertTrue(entities.stream().filter(e -> e.getId().equals("zombie_toast_1"))
                                .anyMatch(e -> e.getPosition().getY() == 7));

                assertTrue(entities.stream().filter(e -> e.getType().equals("spider_1"))
                                .anyMatch(e -> e.getPosition().getX() == 7));
                assertTrue(entities.stream().filter(e -> e.getType().equals("spider_1"))
                                .anyMatch(e -> e.getPosition().getY() == 2));

                for (ItemResponse e : res1.getInventory()) {
                        assertTrue(TestHelper.hasItemByType(res2, e.getType()));
                }

                assertThrows(IllegalArgumentException.class, () -> controller.loadGame("save_2"));
        }

}
