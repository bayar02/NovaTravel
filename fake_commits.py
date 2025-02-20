import os
import random
from datetime import datetime, timedelta
import subprocess
import time
import sys
import shutil

# Git configuration
GIT_USERNAME = "mongigi"
GIT_EMAIL = "medmonji.kraoua@esprit.tn"
REPO_URL = "git@github.com:bayar02/NovaTravel.git"  # Changed to SSH URL
BRANCH_NAME = "monjiji_history"

# Date range - adjust if needed
START_DATE = datetime(2025, 2, 20)
END_DATE = datetime(2025, 3, 6)

# Commit messages for different features
COMMIT_MESSAGES = [
    "feat: Implement flight search functionality",
    "feat: Add reservation management system",
    "feat: Create user authentication module",
    "feat: Add flight booking interface",
    "feat: Implement payment integration",
    "feat: Add flight schedule management",
    "feat: Create passenger information form",
    "feat: Add email confirmation system",
    "feat: Implement flight status tracking",
    "feat: Add admin dashboard",
    "feat: Create reports generation",
    "feat: Add flight price calculator",
    "feat: Implement seat selection system",
    "feat: Add flight notifications",
    "feat: Create booking history view",
    "fix: Resolve login validation issue",
    "fix: Fix flight search filters",
    "fix: Correct payment processing bug",
    "fix: Address reservation confirmation error",
    "fix: Fix date validation in booking form",
    "style: Improve UI layout",
    "style: Update color scheme",
    "style: Enhance form validation feedback",
    "style: Improve responsive design",
    "style: Update navigation menu",
    "docs: Add API documentation",
    "docs: Update README with new features",
    "docs: Add user manual",
    "docs: Document database schema",
    "docs: Add deployment guide",
    "refactor: Optimize database queries",
    "refactor: Improve code structure",
    "refactor: Clean up unused imports",
    "refactor: Enhance error handling",
    "refactor: Optimize performance"
]

def run_git_command(command, check=True):
    """Run a git command safely"""
    try:
        result = subprocess.run(command, check=check, capture_output=True, text=True)
        if result.stderr and not result.stderr.startswith("warning:"):
            print(f"Git command output: {result.stderr}")
        return result
    except subprocess.CalledProcessError as e:
        print(f"Git command failed: {' '.join(command)}")
        print(f"Error: {e.stderr}")
        if check:
            raise
        return e.returncode

def clean_directory():
    """Clean up the workspace"""
    try:
        if os.path.exists("NovaTravel"):
            # On Windows, sometimes we need to change attributes before removing
            for root, dirs, files in os.walk("NovaTravel"):
                for d in dirs:
                    os.chmod(os.path.join(root, d), 0o777)
                for f in files:
                    os.chmod(os.path.join(root, f), 0o777)
            shutil.rmtree("NovaTravel", ignore_errors=True)
            time.sleep(1)  # Give Windows some time
    except Exception as e:
        print(f"Warning: Could not clean directory: {e}")

def setup_git():
    """Set up git repository"""
    # Clean up first
    clean_directory()
    
    # Set git config
    run_git_command(["git", "config", "--global", "user.name", GIT_USERNAME])
    run_git_command(["git", "config", "--global", "user.email", GIT_EMAIL])
    
    # Clone repo
    run_git_command(["git", "clone", REPO_URL])
    os.chdir("NovaTravel")
    
    # Fetch all branches
    run_git_command(["git", "fetch", "origin"])
    
    try:
        # Try to check out existing branch
        run_git_command(["git", "checkout", BRANCH_NAME])
    except:
        # If branch doesn't exist, create it from main
        run_git_command(["git", "checkout", "main"])
        run_git_command(["git", "checkout", "-b", BRANCH_NAME])

def create_fake_changes():
    """Create or modify files"""
    files = [
        os.path.join("src", "main", "java", "tn", "esprit", "controller", "FlightController.java"),
        os.path.join("src", "main", "java", "tn", "esprit", "controller", "ReservationController.java"),
        os.path.join("src", "main", "java", "tn", "esprit", "model", "Flight.java"),
        os.path.join("src", "main", "java", "tn", "esprit", "model", "Reservation.java"),
        os.path.join("src", "main", "resources", "fxml", "main.fxml"),
        os.path.join("src", "main", "resources", "fxml", "flight_search.fxml"),
        os.path.join("src", "main", "resources", "fxml", "reservation.fxml")
    ]
    
    for file in files:
        os.makedirs(os.path.dirname(file), exist_ok=True)
        with open(file, "a") as f:
            f.write(f"\n// Modified at {datetime.now()}\n")

def create_commits():
    """Create commits with dates between START_DATE and END_DATE"""
    # Make sure we're in the right branch
    subprocess.run(["git", "checkout", BRANCH_NAME])
    
    total_days = (END_DATE - START_DATE).days
    
    for i in range(total_days + 1):
        pushes_per_day = random.randint(1, 2)
        
        for push in range(pushes_per_day):
            commits_before_push = random.randint(1, 3)
            
            for commit in range(commits_before_push):
                random_hour = random.randint(9, 18)
                random_minute = random.randint(0, 59)
                random_second = random.randint(0, 59)
                
                commit_time = random_hour * 3600 + random_minute * 60 + random_second
                commit_time += (push * 4 * 3600) + (commit * 30 * 60)
                
                date = START_DATE + timedelta(days=i, seconds=commit_time)
                create_fake_changes()
                
                # Set commit date
                os.environ["GIT_AUTHOR_DATE"] = date.strftime("%Y-%m-%d %H:%M:%S")
                os.environ["GIT_COMMITTER_DATE"] = date.strftime("%Y-%m-%d %H:%M:%S")
                
                # Create commit
                commit_msg = random.choice(COMMIT_MESSAGES)
                subprocess.run(["git", "add", "."])
                subprocess.run(["git", "commit", "-m", commit_msg])
            
            # Push using SSH
            subprocess.run(["git", "push", "origin", BRANCH_NAME])
            time.sleep(1)

def main():
    try:
        setup_git()
        create_commits()
        print("Fake commits created and pushed successfully!")
    except Exception as e:
        print(f"Error: Script failed: {e}")
        sys.exit(1)

if __name__ == "__main__":
    # Make sure we're in the NovaTravel directory
    if not os.path.exists(".git"):
        print("Please run this script from the NovaTravel directory")
        exit(1)
    
    create_commits()
    print("Fake commits created and pushed successfully!")
