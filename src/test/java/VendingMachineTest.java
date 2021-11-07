import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.params.ValueSource;

@DisplayName("Тестирование вендингового аппарата")
class VendingMachineTest {
    private VendingMachine vm;
    private final long passcode = 117345294655382L;

    @BeforeEach
    void setUp() {
        vm = new VendingMachine();
    }

    /**
     * a. Количство продукта 1
     **/
    @Test
    @DisplayName("a. Количство продукта 1 - Корректно")
    void getNumberOfProduct1() {
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(0, vm.getNumberOfProduct1());

        enterAdminMode();
        assertEquals(0, vm.getNumberOfProduct1());
        fillProduct1();
        assertEquals(30, vm.getNumberOfProduct1());
        exitAdminMode();

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(30, vm.getNumberOfProduct1());
    }

    /**
     * b. Количство продукта 2
     **/
    @Test
    @DisplayName("b. Количство продукта 2 - Корректно")
    void getNumberOfProduct2() {
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(0, vm.getNumberOfProduct2());

        enterAdminMode();
        assertEquals(0, vm.getNumberOfProduct2());
        fillProduct2();
        assertEquals(40, vm.getNumberOfProduct2());
        exitAdminMode();

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(40, vm.getNumberOfProduct2());
    }

    /**
     * c. Баланс
     **/
    @Test
    @DisplayName("c. Баланс - Корректно")
    void getCurrentBalance() {
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertEquals(0, vm.getCurrentBalance());
        putCoin1();
        assertEquals(VendingMachine.coinval1, vm.getCurrentBalance());
        putCoin2();
        assertEquals(VendingMachine.coinval1 + VendingMachine.coinval2, vm.getCurrentBalance());
    }

    /**
     * d. Режим
     **/
    @Test
    @DisplayName("d. Режим - Корректно")
    void getCurrentMode() {
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        enterAdminMode();
        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());
        exitAdminMode();

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
    }

    /**
     * e. Баланс монет
     **/
    @ParameterizedTest
    @DisplayName("e. Баланс монет - Корректно")
    @ValueSource(ints = {1, 5, 10})
    void getCurrentSum(int n) {
        fillCoins1(n);
        fillCoins2(n);

        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());
        assertEquals(n * (VendingMachine.coinval1 + VendingMachine.coinval2), vm.getCurrentSum());
    }

    @ParameterizedTest
    @DisplayName("e. Баланс монет - В рабочем режиме")
    @ValueSource(ints = {1, 5, 10})
    void getCurrentSumOperationMode(int n) {
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(0, vm.getCurrentSum());

        fillCoins1(n);
        fillCoins2(n);
        exitAdminMode();

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(0, vm.getCurrentSum());
    }

    /**
     * f. Количество монет 1
     **/
    @ParameterizedTest
    @DisplayName("f. Количество монет 1 - Корректно")
    @ValueSource(ints = {1, 5, 10})
    void getCoins1(int n) {
        fillCoins1(n);

        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());
        assertEquals(n, vm.getCoins1());
    }

    @ParameterizedTest
    @DisplayName("f. Количество монет 1 - В рабочем режиме")
    @ValueSource(ints = {1, 5, 10})
    void getCoins1OperationMode(int n) {
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(0, vm.getCoins1());

        fillCoins1(n);
        exitAdminMode();

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(0, vm.getCoins1());
    }

    /**
     * g. Количество монет 2
     **/
    @ParameterizedTest
    @DisplayName("g. Количество монет 2 - Корректно")
    @ValueSource(ints = {1, 5, 10})
    void getCoins2(int n) {
        fillCoins2(n);

        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());
        assertEquals(n, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("g. Количество монет 2 - В рабочем режиме")
    @ValueSource(ints = {1, 5, 10})
    void getCoins2OperationMode(int n) {
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(0, vm.getCoins2());

        fillCoins2(n);
        exitAdminMode();

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(0, vm.getCoins2());
    }

    /**
     * h. Цена товара 1
     **/
    @ParameterizedTest
    @DisplayName("h. Цена товара 1 - Корректно")
    @ValueSource(ints = {1, 5, 10})
    void getPrice1(int p) {
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(8, vm.getPrice1());

        enterAdminMode();
        assertEquals(8, vm.getPrice1());
        setPrice1(p);
        assertEquals(p, vm.getPrice1());
        exitAdminMode();

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(p, vm.getPrice1());
    }

    /**
     * i. Цена товара 2
     **/
    @ParameterizedTest
    @DisplayName("i. Цена товара 2 - Корректно")
    @ValueSource(ints = {1, 6, 10})
    void getPrice2(int p) {
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(5, vm.getPrice2());

        enterAdminMode();
        assertEquals(5, vm.getPrice2());
        setPrice2(p);
        assertEquals(p, vm.getPrice2());
        exitAdminMode();

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(p, vm.getPrice2());
    }

    /**
     * j. Заполнение продукта 1
     **/
    @Test
    @DisplayName("j. Заполнение продукта 1 - Корректно")
    void fillProduct1() {
        enterAdminMode();
        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());

        assertNotEquals(30, vm.getNumberOfProduct1());
        assertEquals(VendingMachine.Response.OK, vm.fillProduct1());
        assertEquals(30, vm.getNumberOfProduct1());
    }

    @Test
    @DisplayName("j. Заполнение продукта 1 - В рабочем режиме")
    void fillProduct1WrongMode() {
        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertNotEquals(30, vm.getNumberOfProduct1());
        assertEquals(VendingMachine.Response.ILLEGAL_OPERATION, vm.fillProduct1());
        assertNotEquals(30, vm.getNumberOfProduct1());
    }

    /**
     * k. Заполение продукта 2
     **/
    @Test
    @DisplayName("k. Заполение продукта 2 - Корректно")
    void fillProduct2() {
        enterAdminMode();
        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());

        assertNotEquals(40, vm.getNumberOfProduct2());
        assertEquals(VendingMachine.Response.OK, vm.fillProduct2());
        assertEquals(40, vm.getNumberOfProduct2());
    }

    @Test
    @DisplayName("k. Заполение продукта 2 - В рабочем режиме")
    void fillProduct2WrongMode() {
        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertNotEquals(40, vm.getNumberOfProduct2());
        assertEquals(VendingMachine.Response.ILLEGAL_OPERATION, vm.fillProduct2());
        assertNotEquals(40, vm.getNumberOfProduct2());
    }

    /**
     * l. Заполение монетами 1
     **/
    @ParameterizedTest
    @DisplayName("l. Заполение монетами 1 - Корректно")
    @ValueSource(ints = {1, 50, 10})
    void fillCoins1(int n) {
        enterAdminMode();
        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());

        assertNotEquals(n, vm.getCoins1());
        assertEquals(VendingMachine.Response.OK, vm.fillCoins1(n));
        assertEquals(n, vm.getCoins1());
    }

    @ParameterizedTest
    @DisplayName("l. Заполение монетами 1 - Неверное количество")
    @ValueSource(ints = {-1, 0, 51})
    void fillCoins1InvalidParam(int n) {
        int startValue = vm.getCoins1();

        enterAdminMode();
        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.INVALID_PARAM, vm.fillCoins1(n));
        assertEquals(startValue, vm.getCoins1());
    }

    @ParameterizedTest
    @DisplayName("l. Заполение монетами 1 - В рабочем режиме")
    @ValueSource(ints = {1, 50, 10})
    void fillCoins1WrongMode(int n) {
        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertNotEquals(n, vm.getCoins1());
        assertEquals(VendingMachine.Response.ILLEGAL_OPERATION, vm.fillCoins1(n));
        assertNotEquals(n, vm.getCoins1());
    }

    /**
     * m. Заполение монетами 2
     **/
    @ParameterizedTest
    @DisplayName("m. Заполение монетами 2 - Корректно")
    @ValueSource(ints = {1, 50, 10})
    void fillCoins2(int n) {
        enterAdminMode();
        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());

        assertNotEquals(n, vm.getCoins2());
        assertEquals(VendingMachine.Response.OK, vm.fillCoins2(n));
        assertEquals(n, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("m. Заполение монетами 2 - Неверное количество")
    @ValueSource(ints = {-1, 0, 51})
    void fillCoins2InvalidParam(int n) {
        enterAdminMode();
        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());

        int startValue = vm.getCoins2();

        assertEquals(VendingMachine.Response.INVALID_PARAM, vm.fillCoins2(n));
        assertEquals(startValue, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("m. Заполение монетами 2 - В рабочем режиме")
    @ValueSource(ints = {1, 50, 10})
    void fillCoins2WrongMode(int n) {
        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertNotEquals(n, vm.getCoins2());
        assertEquals(VendingMachine.Response.ILLEGAL_OPERATION, vm.fillCoins2(n));
        assertNotEquals(n, vm.getCoins2());
    }

    @Test
    @DisplayName("m. Добавление монеты 2 - Переполнение")
    void putCoin2Overflow() {
        fillCoins2(50);
        int startValue  = vm.getCurrentBalance();

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.CANNOT_PERFORM, vm.putCoin2());
        assertEquals(startValue, vm.getCurrentBalance());
    }

    /**
     * n. Вход в режим отладки
     **/
    @Test
    @DisplayName("n. Вход в режим отладки - Корректно")
    void enterAdminMode() {
        if (vm.getCurrentMode() != VendingMachine.Mode.OPERATION)
            exitAdminMode();

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(VendingMachine.Response.OK, vm.enterAdminMode(passcode));
        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());
    }

    @Test
    @DisplayName("n. Вход в режим отладки - Неверный код")
    void enterAdminModeInvalidCode() {
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(VendingMachine.Response.INVALID_PARAM, vm.enterAdminMode(-1));
        assertNotEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());
    }

    @Test
    @DisplayName("n. Вход в режим отладки - Внесены средства")
    void enterAdminModeWithBalance() {
        putCoin1();
        assertTrue(vm.getCurrentBalance() > 0);

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(VendingMachine.Response.CANNOT_PERFORM, vm.enterAdminMode(passcode));
        assertNotEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());
    }

    /**
     * o. Выход из режима отладки
     **/
    @Test
    @DisplayName("о. Выход из режима отладки - Корректно")
    void exitAdminMode() {
        if (vm.getCurrentMode() != Mode.ADMINISTERING)
            enterAdminMode();

        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());
        vm.exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
    }

    /**
     * p. Установка цены продукта 1
     **/
    @ParameterizedTest
    @DisplayName("p. Установка цены продукта 1 - Корректно")
    @ValueSource(ints = {1, 10, 50})
    void setPrice1(int p) {
        enterAdminMode();
        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());

        assertNotEquals(p, vm.getPrice1());
        assertEquals(VendingMachine.Response.OK, vm.setPrice1(p));
        assertEquals(p, vm.getPrice1());
    }

    @ParameterizedTest
    @DisplayName("p. Установка цены продукта 1 - В рабочем режиме")
    @ValueSource(ints = {1, 50, 10})
    void setPrice1WrongMode(int p) {
        exitAdminMode();

        assertNotEquals(p, vm.getPrice1());
        assertEquals(VendingMachine.Response.ILLEGAL_OPERATION, vm.setPrice1(p));
        assertNotEquals(p, vm.getPrice1());
    }

    @ParameterizedTest
    @DisplayName("p. Установка цены продукта 1 - Неверная цена")
    @ValueSource(ints = {-10, 0})
    void setPrice1InvalidParams(int p) {
        enterAdminMode();

        assertNotEquals(p, vm.getPrice1());
        assertEquals(VendingMachine.Response.INVALID_PARAM, vm.setPrice1(p));
        assertNotEquals(p, vm.getPrice1());
    }

    /**
     * q. Установка цены продукта 2
     **/
    @ParameterizedTest
    @DisplayName("q. Установка цены продукта 2 - Корректно")
    @ValueSource(ints = {1, 50, 10})
    void setPrice2(int p) {
        enterAdminMode();

        assertNotEquals(p, vm.getPrice2());
        assertEquals(VendingMachine.Response.OK, vm.setPrice2(p));
        assertEquals(p, vm.getPrice2());
    }

    @ParameterizedTest
    @DisplayName("q. Установка цены продукта 2 - В рабочем режиме")
    @ValueSource(ints = {1, 50, 10})
    void setPrice2WrongMode(int p) {
        exitAdminMode();

        assertNotEquals(p, vm.getPrice2());
        assertEquals(VendingMachine.Response.ILLEGAL_OPERATION, vm.setPrice2(p));
        assertNotEquals(p, vm.getPrice2());
    }

    @ParameterizedTest
    @DisplayName("q. Установка цены продукта 2 - Неверная цена")
    @ValueSource(ints = {-10, 0})
    void setPrice2InvalidParams(int p) {
        enterAdminMode();

        assertNotEquals(p, vm.getPrice2());
        assertEquals(VendingMachine.Response.INVALID_PARAM, vm.setPrice2(p));
        assertNotEquals(p, vm.getPrice2());
    }

    /**
     * r. Добавление монеты 1
     **/
    @Test
    @DisplayName("r. Добавление монеты 1 - Корректно")
    void putCoin1() {
        int startValue = vm.getCurrentBalance();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.OK, vm.putCoin1());
        assertEquals(startValue  + VendingMachine.coinval1, vm.getCurrentBalance());
    }

    @Test
    @DisplayName("r. Добавление монеты 1 - В режиме отладки")
    void putCoin1WrongMode() {
        int startValue = vm.getCurrentBalance();

        enterAdminMode();
        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.ILLEGAL_OPERATION, vm.putCoin1());
        assertEquals(startValue, vm.getCurrentBalance());
    }

    @Test
    @DisplayName("r. Добавление монеты 1 - Переполнение")
    void putCoin1Overflow() {
        fillCoins1(50);
        int startValue = vm.getCoins1();

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.CANNOT_PERFORM, vm.putCoin1());

        enterAdminMode();
        assertEquals(startValue, vm.getCoins1());
    }

    /**
     * s. Добавление монеты 2
     **/
    @Test
    @DisplayName("s. Добавление монеты 2 - Корректно")
    void putCoin2() {
        int startValue  = vm.getCurrentBalance();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.OK, vm.putCoin2());
        assertEquals(startValue + VendingMachine.coinval2, vm.getCurrentBalance());
    }

    @Test
    @DisplayName("s. Добавление монеты 2 - В режиме отладки")
    void putCoin2WrongMode() {
        int startValue  = vm.getCurrentBalance();

        enterAdminMode();
        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.ILLEGAL_OPERATION, vm.putCoin1());
        assertEquals(startValue, vm.getCurrentBalance());
    }

    /**
     * t. Возврат монет
     **/
    @Test
    @DisplayName("t. Возврат монет - Корректно, нулевой баланс")
    void returnMoneyZeroBalance() {
        fillCoins1(1);
        fillCoins2(1);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertEquals(0, vm.getCurrentBalance());
        assertEquals(VendingMachine.Response.OK, vm.returnMoney());

        enterAdminMode();
        assertEquals(1, vm.getCoins1());
        assertEquals(1, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("t. Возврат монет - Корректно, баланс больше суммарной стоимости монет 2 вида")
    @ValueSource(ints = {5, 9, 13})
    void returnMoneyMoreThanSumOfCoins2(int n) {
        fillCoins2(n / 2 - 1);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n; i++) putCoin1();

        assertEquals(n, vm.getCurrentBalance());
        assertEquals(VendingMachine.Response.OK, vm.returnMoney());
        assertEquals(0, vm.getCurrentBalance());

        enterAdminMode();
        assertEquals(n - 3, vm.getCoins1());
        assertEquals(0, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("t. Возврат монет - Корректно, четный баланс")
    @ValueSource(ints = {4, 10, 14})
    void returnMoneyEvenBalance(int n) {
        fillCoins2(n);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n; i++) putCoin1();

        assertEquals(n, vm.getCurrentBalance());
        assertEquals(VendingMachine.Response.OK, vm.returnMoney());
        assertEquals(0, vm.getCurrentBalance());
        // Должны выдаться все монеты 2 вида

        enterAdminMode();
        assertEquals(n, vm.getCoins1());
        assertEquals(n / 2, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("t. Возврат монет - Корректно, не хватает монет 2 вида")
    @ValueSource(ints = {8, 10, 14})
    void returnMoneyEvenBalanceNotEnoughCoin2(int n) {
        fillCoins2(n / 2 - 2);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n; i++) putCoin1();

        assertEquals(n, vm.getCurrentBalance());
        assertEquals(VendingMachine.Response.OK, vm.returnMoney());
        assertEquals(0, vm.getCurrentBalance());
        // Должны выдаться все монеты 2 вида

        enterAdminMode();
        assertEquals(n - 4, vm.getCoins1());
        assertEquals(0, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("t. Возврат монет - Корректно, нечетный баланс")
    @ValueSource(ints = {5, 11, 15})
    void returnMoneyOddBalance(int n) {
        fillCoins2(n);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n; i++) putCoin1();

        assertEquals(n, vm.getCurrentBalance());
        assertEquals(VendingMachine.Response.OK, vm.returnMoney());
        assertEquals(0, vm.getCurrentBalance());
        // Должны выдаться все монеты 2 вида

        enterAdminMode();
        assertEquals(n - 1, vm.getCoins1());
        assertEquals(n / 2 + 1, vm.getCoins2());
    }

    @Test
    @DisplayName("t. Возврат монет - В режиме отладки")
    void returnMoneyWrongMode() {
        fillCoins1(1);
        fillCoins2(1);

        assertNotEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.ILLEGAL_OPERATION, vm.returnMoney());

        assertEquals(1, vm.getCoins1());
        assertEquals(1, vm.getCoins2());
    }

    /**
     * u. Выдача товара 1
     **/
    @ParameterizedTest
    @DisplayName("u. Выдача товара 1 - Корректно, без сдачи")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct1NoChange(int n) {
        fillProduct1();

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n * 4; i++) putCoin2();

        assertEquals(8 * n, vm.getCurrentBalance());
        assertEquals(VendingMachine.Response.OK, vm.giveProduct1(n));
        assertEquals(0, vm.getCurrentBalance());

        enterAdminMode();
        assertEquals(30 - n, vm.getNumberOfProduct1());
    }

    @ParameterizedTest
    @DisplayName("u. Выдача товара 1 - Корректно, четная сдача")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct1EvenChange(int n) {
        fillProduct1();
        fillCoins2(5);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n * 8 + 4; i++) putCoin1();

        assertEquals(n * 8 + 4, vm.getCurrentBalance());
        assertEquals(VendingMachine.Response.OK, vm.giveProduct1(n));
        assertEquals(0, vm.getCurrentBalance());

        enterAdminMode();
        assertEquals(30 - n, vm.getNumberOfProduct1());
        assertEquals(3, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("u. Выдача товара 1 - Корректно, четная сдача, не хватает монет 2 вида")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct1EvenChangeNotEnoughCoin2(int n) {
        fillProduct1();
        fillCoins2(1);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n * 8 + 4; i++) putCoin1();

        assertEquals(VendingMachine.Response.OK, vm.giveProduct1(n));
        assertEquals(0, vm.getCurrentBalance());

        enterAdminMode();
        assertEquals(30 - n, vm.getNumberOfProduct1());
        assertEquals(n * 8 + 2, vm.getCoins1());
        assertEquals(0, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("u. Выдача товара 1 - Корректно, нечетная сдача")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct1OddChange(int n) {
        fillProduct1();
        fillCoins2(5);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n * 8 + 5; i++) putCoin1();

        assertEquals(VendingMachine.Response.OK, vm.giveProduct1(n));
        assertEquals(0, vm.getCurrentBalance());

        enterAdminMode();
        assertEquals(30 - n, vm.getNumberOfProduct1());
        assertEquals(n * 8 + 4, vm.getCoins1());
        assertEquals(3, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("u. Выдача товара 1 - Неверное количество")
    @ValueSource(ints = {0, -1, 31})
    void giveProduct1InvalidParam(int n) {
        fillProduct1();

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.INVALID_PARAM, vm.giveProduct1(n));

        enterAdminMode();
        assertEquals(30, vm.getNumberOfProduct1());
    }

    @ParameterizedTest
    @DisplayName("u. Выдача товара 1 - Недостаточно товара")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct1NotEnoughProduct(int n) {
        enterAdminMode();
        assertTrue(vm.getNumberOfProduct1() < n);
        exitAdminMode();

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(VendingMachine.Response.INSUFFICIENT_PRODUCT, vm.giveProduct1(n));
    }

    @ParameterizedTest
    @DisplayName("u. Выдача товара 1 - Недостаточно средств")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct1NotEnoughMoney(int n) {
        fillProduct1();

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.INSUFFICIENT_MONEY, vm.giveProduct1(n));

        enterAdminMode();
        assertEquals(30, vm.getNumberOfProduct1());
    }

    @ParameterizedTest
    @DisplayName("u. Выдача товара 1 - Не хватает сдачи")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct1UnsuitableChange(int n) {
        fillProduct1();
        setPrice1(7);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n * 4; i++) putCoin2();

        assertEquals(VendingMachine.Response.UNSUITABLE_CHANGE, vm.giveProduct1(n));
        assertEquals(n * 8, vm.getCurrentBalance());

        vm.returnMoney();

        enterAdminMode();
        assertEquals(30, vm.getNumberOfProduct1());
    }

    @Test
    @DisplayName("u. Выдача товара 1 - В режиме отладки")
    void giveProduct1WrongMode() {
        fillProduct1();

        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());
        assertEquals(VendingMachine.Response.ILLEGAL_OPERATION, vm.giveProduct1(1));

        assertEquals(30, vm.getNumberOfProduct1());
    }

    /**
     * v. Выдача товара 2
     **/
    @ParameterizedTest
    @DisplayName("v. Выдача товара 2 - Корректно, без сдачи")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct2NoChange(int n) {
        fillProduct2();

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n * 5; i++) putCoin1();

        assertEquals(5 * n, vm.getCurrentBalance());
        assertEquals(VendingMachine.Response.OK, vm.giveProduct2(n));
        assertEquals(0, vm.getCurrentBalance());

        enterAdminMode();
        assertEquals(40 - n, vm.getNumberOfProduct2());
    }

    @ParameterizedTest
    @DisplayName("v. Выдача товара 2 - Корректно, четная сдача")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct2EvenChange(int n) {
        fillProduct2();
        fillCoins2(5);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n * 5 + 4; i++) putCoin1();

        assertEquals(n * 5 + 4, vm.getCurrentBalance());
        assertEquals(VendingMachine.Response.OK, vm.giveProduct2(n));
        assertEquals(0, vm.getCurrentBalance());

        enterAdminMode();
        assertEquals(40 - n, vm.getNumberOfProduct2());
        assertEquals(3, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("v. Выдача товара 2 - Корректно, четная сдача, не хватает монет 2 вида")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct2EvenChangeNotEnoughCoin2(int n) {
        fillProduct2();
        fillCoins2(1);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n * 5 + 4; i++) putCoin1();

        assertEquals(n * 5 + 4, vm.getCurrentBalance());
        assertEquals(VendingMachine.Response.OK, vm.giveProduct2(n));
        assertEquals(0, vm.getCurrentBalance());

        enterAdminMode();
        assertEquals(40 - n, vm.getNumberOfProduct2());
        assertEquals(n * 5 + 2, vm.getCoins1());
        assertEquals(0, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("v. Выдача товара 2 - Корректно, нечетная сдача")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct2OddChange(int n) {
        fillProduct2();
        fillCoins2(5);

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n * 5 + 5; i++) putCoin1();

        assertEquals(n * 5 + 5, vm.getCurrentBalance());
        assertEquals(VendingMachine.Response.OK, vm.giveProduct2(n));
        assertEquals(0, vm.getCurrentBalance());

        enterAdminMode();
        assertEquals(40 - n, vm.getNumberOfProduct2());
        assertEquals(n * 5 + 4, vm.getCoins1());
        assertEquals(3, vm.getCoins2());
    }

    @ParameterizedTest
    @DisplayName("v. Выдача товара 2 - Неверное количество")
    @ValueSource(ints = {0, -1, 41})
    void giveProduct2InvalidParam(int n) {
        fillProduct2();

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.INVALID_PARAM, vm.giveProduct2(n));

        enterAdminMode();
        assertEquals(40, vm.getNumberOfProduct2());
    }

    @ParameterizedTest
    @DisplayName("v. Выдача товара 2 - Недостаточно товара")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct2NotEnoughProduct(int n) {
        enterAdminMode();
        assertTrue(vm.getNumberOfProduct2() < n);
        exitAdminMode();

        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());
        assertEquals(VendingMachine.Response.INSUFFICIENT_PRODUCT, vm.giveProduct2(n));
    }

    @ParameterizedTest
    @DisplayName("v. Выдача товара 2 - Недостаточно средств")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct2NotEnoughMoney(int n) {
        fillProduct2();

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        assertEquals(VendingMachine.Response.INSUFFICIENT_MONEY, vm.giveProduct2(n));

        enterAdminMode();
        assertEquals(40, vm.getNumberOfProduct2());
    }

    @ParameterizedTest
    @DisplayName("v. Выдача товара 2 - Не хватает сдачи")
    @ValueSource(ints = {1, 3, 5})
    void giveProduct2UnsuitableChange(int n) {
        fillProduct2();

        exitAdminMode();
        assertEquals(VendingMachine.Mode.OPERATION, vm.getCurrentMode());

        for (int i = 0; i < n * 3; i++) putCoin2();

        assertEquals(VendingMachine.Response.UNSUITABLE_CHANGE, vm.giveProduct2(n));
        assertEquals(n * 3 * 2, vm.getCurrentBalance());

        vm.returnMoney();

        enterAdminMode();
        assertEquals(40, vm.getNumberOfProduct2());
    }

    @Test
    @DisplayName("v. Выдача товара 2 - В режиме отладки")
    void giveProduct2WrongMode() {
        fillProduct2();

        assertEquals(VendingMachine.Mode.ADMINISTERING, vm.getCurrentMode());
        assertEquals(VendingMachine.Response.ILLEGAL_OPERATION, vm.giveProduct2(1));

        assertEquals(40, vm.getNumberOfProduct2());
    }
}