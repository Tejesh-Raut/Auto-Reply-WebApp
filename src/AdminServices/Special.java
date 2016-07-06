package AdminServices;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * @author Tejesh_Raut
 *
 */
public class Special 
{
	public static String Reply(String a , String b, String c)
	{
		String ans="";
		if(a == "Price")
		{
			//find the cost of flight from b to c
			System.out.println("Scrapping has started");
			/*
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("permissions.default.image", 2);
			profile.setPreference("permissions.default.stylesheet", 2);
			profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", false);
			*/
			System.setProperty("webdriver.chrome.driver","/home/techmahindra/workspace/TejeshAutoReplyWeb/WebDriver/chromedriver");
			WebDriver driver = new ChromeDriver();
			driver.get("https://book.airindia.in/itd/itd/Air/");
			driver.findElement(By.xpath("//select[@id='wdforigin1']/option[contains(text(), '" + b + "')]")).click();
			driver.findElement(By.xpath("//select[@id='wdfdest1']/option[contains(text(), '" + c + "')]")).click();
			driver.findElement(By.xpath(".//*[@id='mainform']/table/tbody/tr[3]/td/table/tbody/tr/td/input")).click();
			WebDriverWait wait2 = new WebDriverWait(driver, 40);
			WebElement element2 = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='mainform']/table/tbody/tr[8]/td/table/tbody/tr/td/input[3]")));
			driver.findElement(By.xpath(".//*[@id='mainform']/table/tbody/tr[8]/td/table/tbody/tr/td/input[3]")).click();
			WebDriverWait wait3 = new WebDriverWait(driver, 40);
			WebElement element3 = wait3.until(ExpectedConditions.visibilityOfElementLocated(By.className("totaloriginalprice")));
			String price = driver.findElement(By.className("totaloriginalprice")).getText();
			ans = "Total price including taxes is " + price;
			driver.close();
		}
		return ans;
	}
}
