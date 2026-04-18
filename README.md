# SauceDemo Test Automation Framework

A Selenium-based test automation framework for [SauceDemo](https://www.saucedemo.com), built with Java, TestNG, and Allure reporting.

---

## 🏗️ Project Structure

```
src/
├── test/
│   ├── java/
│   │   ├── Testcases/
│   │   │   ├── TestBase.java                        # Base class for all tests
│   │   │   ├── TC01_LoginPage.java                  # Login tests (positive & negative)
│   │   │   ├── TC02_Products.java                   # Cart badge verification
│   │   │   ├── TC03_CheckOut_Fill_Information.java  # Checkout step one
│   │   │   └── TC04_CheckOut_Overview.java          # Checkout step two & order confirmation
│   │   ├── drivers/
│   │   │   ├── DriverFactory.java                   # WebDriver instantiation (Chrome, Firefox, Edge, Grid)
│   │   │   └── DriverHolder.java                    # ThreadLocal WebDriver storage
│   │   ├── pages/
│   │   │   ├── PageBase.java                        # Base page with shared utilities & waits
│   │   │   ├── P01_LoginPage.java                   # Login page actions
│   │   │   ├── P02_ProductsPage.java                # Products/inventory page actions
│   │   │   ├── P03_Checkout_Fill_Information.java   # Checkout step one actions
│   │   │   └── P04_CheckoutOverview.java            # Checkout step two actions
│   │   ├── retrytests/
│   │   │   └── RetryAnalyzer.java                   # TestNG retry logic (1 retry on failure)
│   │   ├── listeners/
│   │   │   └── CustomListener.java                  # TestNG event listener
│   │   └── utli/
│   │       └── Utility.java                         # JSON reader, random data generators
│   └── resources/
│       ├── properties/
│       │   └── environment.properties               # Project URL & name config
│       ├── testdata/
│       │   └── Credential.json                      # Valid & invalid login credentials
│       ├── log4j.xml                                # Log4j logging configuration
│       └── testng.xml                               # TestNG suite configuration
```

---

## 🧪 Test Cases

| Class | Test Method | Description | Severity |
|---|---|---|---|
| `TC01_LoginPage` | `loginToSauceDemo_P` | Login with valid credentials | CRITICAL |
| `TC01_LoginPage` | `loginToSauceDemo_N` | Login with invalid credentials | NORMAL |
| `TC02_Products` | `verifyCartBadgeReflectsAddedProducts` | Cart badge matches added product count | CRITICAL |
| `TC03_CheckOut_Fill_Information` | `fillInformation` | Fill checkout form with random data | CRITICAL |
| `TC04_CheckOut_Overview` | `verifySuccessMessage` | Complete order and verify success message | BLOCKER |

---

## 🛠️ Tech Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 22 | Programming language |
| Selenium WebDriver | 4.29.0 | Browser automation |
| TestNG | Latest | Test framework & execution |
| Allure | Latest | Test reporting |
| Log4j | Latest | Logging |
| JSON Simple | Latest | Test data parsing |

---

## ⚙️ Configuration

### `environment.properties`
```properties
projectName=SauceDemo
url=https://www.saucedemo.com
```

### `Credential.json`
```json
{
  "Username":        "standard_user",
  "Password":        "secret_sauce",
  "InvalidUsername": "invalid_user",
  "InvalidPassword": "wrong_password"
}
```

### `testng.xml`
```xml
<suite name="SauceDemo Suite">
    <parameter name="browsername" value="chrome"/>
    <test name="Login Tests">
        <classes>
            <class name="Testcases.TC01_LoginPage"/>
        </classes>
    </test>
    ...
</suite>
```

---

## 🚀 How to Run

### Run via TestNG XML
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Run a single test class
```bash
mvn test -Dtest=TC01_LoginPage
```

### Run with a specific browser
```bash
mvn test -Dbrowsername=firefox
```

### Supported browsers
| Value | Browser |
|---|---|
| `chrome` | Google Chrome (default) |
| `firefox` | Mozilla Firefox |
| `edge` | Microsoft Edge |
| `chrome-headless` | Chrome without UI |
| `firefox-headless` | Firefox without UI |
| `grid` | Selenium Grid (localhost:4444) |

---

## 📊 Allure Report

### Generate & open report
```bash
# Run tests first
mvn test

# Generate Allure report
allure generate target/allure-results --clean -o target/allure-report

# Open in browser
allure open target/allure-report
```

### Allure Report Structure
| Epic | Story | Feature |
|---|---|---|
| Login | Login Story | Valid & Invalid Login |
| Shopping Cart | Cart Badge Story | Add to Cart |
| Checkout | Fill Information Story | Checkout Step One |
| Checkout | Overview Story | Checkout Step Two |

---

## 🏛️ Framework Design

### Page Object Model (POM)
Each page in the application has a dedicated class under `pages/` that contains:
- **Locators** — `private final By` fields
- **Actions** — methods that interact with the page and return `this` for method chaining

```java
new P01_LoginPage(getDriver())
    .EnterUserName(username)
    .EnterPassword(password)
    .ClickonLoginButton();
```

### ThreadLocal WebDriver
`DriverHolder` uses `ThreadLocal<WebDriver>` to ensure each test thread has its own isolated browser instance, enabling safe parallel execution.

```java
// Set driver
DriverHolder.setDriver(DriverFactory.getNewInstance("chrome"));

// Get driver anywhere
DriverHolder.getDriver();

// Remove after test (prevents memory leaks)
DriverHolder.removeDriver();
```

### Shared Login Helper
`TestBase` provides `loginWithValidCredentials()` to avoid duplicating login code across test classes:

```java
// Any test class that extends TestBase can call:
loginWithValidCredentials();
```

### Retry Analyzer
Failed tests are retried **once** automatically via `RetryAnalyzer`:
```java
@Test(retryAnalyzer = RetryAnalyzer.class)
```

---

## 📋 Prerequisites

- Java JDK 22+
- Maven 3.8+
- Google Chrome (latest)
- ChromeDriver (auto-managed)
- Allure CLI (for reports)

---

## 👤 Author

SauceDemo Automation Framework
