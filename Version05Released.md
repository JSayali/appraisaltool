# Introduction #

Finally, after month of hard work (almost over nights), basic funcionality have been implemnted. This release includes all Administrator and Quality managers usecases, which means that you can manage users, projects and define CMMI models and rating methods. Full description of use cases is in .vpp project in repository.

No graphics style is created, but application uses jQuery, Uni-Form forms for easy skin/theme creation. I am not really good in graphics, but application is prepared for this.

# Features of 0.5 #

List of features grouped by roles. Detailed description can be found in use-cases.

## Generic ##

  * Login in / Log out, using Spring Security. Application is secured.
  * Remember-me function, so you are not logged out, after your session expires.

  * All tables are AJAX sortable
  * AJAX confirmations of actions.
  * jQuery and many jQuery plugins enhaced (jTree, jDialog, ...)
  * Forms validation
  * Breadcrumbs for easy navigation


## Administrator ##

  * CRUD for users (definig name, e-mail, application role)
  * CRUD for organizations (name, contact information)

### CMMI models definition ###

CRUD for CMMI models (eg. CMMI 1.2-DEV). Defining of model consists of 3 steps

  * Basic attributes definition (name, highest ML, ...)
  * Process groups definition
  * Complete artifact tree definition (Process Area > Goal > Practice > Artifact (in/direct))

### CMMI rating method definiton ###

CRUD for CMMI rating methods (eg. SCAMPI v1.2). Definig method consists of 3 steps

  * Define rated attributes / areas
  * Define scales (including scores)
  * Define aggregation rules

## Quality manager ##

### CRUD for Projects ###

  * managing appraisal projects for existing organizations
  * Every project have name, maturity level and project team
  * assigning users to appraisal projects under project roles (member, leader, auditor)

# What is under construction #

**Conduct appraisal tasks, collecting artefacts, reviewing project and so on.** Will come soon

# Bugs #

Please report any found bugs to Issues tab, thank you.