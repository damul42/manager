# AX Automation Project Guide (English)

## 1. Directory Structure
Maintain this structure so the AI can navigate and generate documents correctly.
```text
天Manager/
戍式式 requirements/           # [INPUT] Raw data (Emails, Meeting Minutes)
弛   戍式式 raw/                # Original source files
弛   戌式式 context.md          # Master context for the AI
戍式式 docs/                   # [OUTPUT] AI-generated results
弛   戍式式 prd/                # PRD storage
弛   戍式式 srs/                # SRS storage
弛   戌式式 api/                # API Specifications (Swagger/OpenAPI)
戍式式 templates/              # [GUIDE] Standard templates
弛   戍式式 prd_template_en.md  # Standard PRD format
弛   戌式式 srs_template_en.md  # Standard SRS format
戌式式 ...