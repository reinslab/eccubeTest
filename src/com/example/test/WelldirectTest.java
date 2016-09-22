package com.example.test;

import java.io.File;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;

public class WelldirectTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	//System.setProperty("webdriver.gecko.driver", "drivers/geckodriver");
    //driver = new FirefoxDriver();
    driver = new ChromeDriver();
    baseUrl = "http://well-direct.com";
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  }

  @Test
  public void testWelldirect() throws Exception {

	// トップページ
    driver.get(baseUrl);
    
    // キッチンツール → 調理器具選択
    Actions action = new Actions(driver);
    WebElement mouseOver = driver.findElement(By.xpath("//nav[@id = 'category']/ul/li[2]"));
    action.moveToElement(mouseOver).build().perform();    
    driver.findElement(By.linkText("調理器具")).click();

    // 商品一覧画面 → 商品選択
    driver.findElement(By.id("result_list__image--2")).click();
    
    // カートに入れる
    driver.findElement(By.cssSelector("li.col-xs-12.col-sm-8 > #cart")).click();
//  driver.findElement(By.xpath("//ul[@id='detail_cart_box__insert_button']//button[.='カートに入れる']")).click();    

    // スクリーンショット取得
    File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
    FileHandler.copy(screenshotFile, new File("./screenshot001.png"));
    
    // カートの中身を削除する
    driver.findElement(By.cssSelector("#cart_item_list__cart_remove > a > svg.cb.cb-close > use")).click();
    assertTrue(closeAlertAndGetItsText().matches("^カートから商品を削除してもよろしいですか[\\s\\S]$"));
    
    // スクリーンショット取得
    screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
    FileHandler.copy(screenshotFile, new File("./screenshot002.png"));
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
