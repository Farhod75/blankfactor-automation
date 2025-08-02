# BlankFactor Website Automation

This project contains Selenium WebDriver automation tests for the BlankFactor website, focusing on an end-to-end flow for the Retirement and Wealth section.

## Features

*   Navigating to the BlankFactor homepage and accepting cookie policy.
*   Navigating to the "Industries" section and then to "Retirement and Wealth".
*   Interacting with and extracting text from dynamic (flip) tiles.
*   Navigating to the "Contact Us" page via the "Let's get started" button.
*   Verifying page URLs and titles.

## Technologies Used

*   Java (JDK 11+)
*   Selenium WebDriver (Java)
*   TestNG
*   Maven
*   WebDriverManager

## Prerequisites

Before running the tests, ensure you have the following installed:

*   Java Development Kit (JDK) 11 or higher
*   Maven (version 3.6.3 or higher)
*   Google Chrome browser

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/yourusername/blankfactor-automation.git
    cd blankfactor-automation
    ```
2.  **Configure JAVA_HOME:** Ensure your `JAVA_HOME` environment variable points to your JDK installation.

## How to Run Tests

To execute the end-to-end test:

*   **Using Maven:**
    ```bash
    mvn clean test
    ```

## Test Reports

TestNG HTML reports will be generated in the `target/surefire-reports` directory after execution.

---