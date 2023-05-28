package nl.basjes.codeowners;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static nl.basjes.codeowners.TestUtils.assertOwners;

class TestCodeOwners {

    private static final Logger LOG = LoggerFactory.getLogger(TestCodeOwners.class);

    private static final String CODEOWNERS =
        "# Lines that start with `#` are ignored.\n" +
        "# app/ @commented-rule\n" +
        "\n" +
        "# Specify a default Code Owner by using a wildcard:\n" +
        "* @default-codeowner\n" +
        "\n" +
        "# Specify multiple Code Owners by using a tab or space:\n" +
        "* @multiple @code @owners\n" +
        "\n" +
        "# Rules defined later in the file take precedence over the rules\n" +
        "# defined before.\n" +
        "# For example, for all files with a filename ending in `.rb`:\n" +
        "*.rb @ruby-owner\n" +
        "\n" +
        "# Files with a `#` can still be accessed by escaping the pound sign:\n" +
        "\\#file_with_pound.rb @owner-file-with-pound\n" +
        "\n" +
        "# Specify multiple Code Owners separated by spaces or tabs.\n" +
        "# In the following case the CODEOWNERS file from the root of the repo\n" +
        "# has 3 Code Owners (@multiple @code @owners):\n" +
        "CODEOWNERS @multiple @code @owners\n" +
        "\n" +
        "# You can use both usernames or email addresses to match\n" +
        "# users. Everything else is ignored. For example, this code\n" +
        "# specifies the `@legal` and a user with email `janedoe@gitlab.com` as the\n" +
        "# owner for the LICENSE file:\n" +
        "LICENSE @legal this_does_not_match janedoe@gitlab.com\n" +
        "\n" +
        "# Use group names to match groups, and nested groups to specify\n" +
        "# them as owners for a file:\n" +
        "README @group @group/with-nested/subgroup\n" +
        "\n" +
        "# End a path in a `/` to specify the Code Owners for every file\n" +
        "# nested in that directory, on any level:\n" +
        "/docs/ @all-docs\n" +
        "\n" +
        "# End a path in `/*` to specify Code Owners for every file in\n" +
        "# a directory, but not nested deeper. This code matches\n" +
        "# `docs/index.md` but not `docs/projects/index.md`:\n" +
        "/docs/* @root-docs\n" +
        "\n" +
        "# Include `/**` to specify Code Owners for all subdirectories\n" +
        "# in a directory. This rule matches `docs/projects/index.md` or\n" +
        "# `docs/development/index.md`\n" +
        "/docs/**/*.md @root-docs\n" +
        "\n" +
        "# This code makes matches a `lib` directory nested anywhere in the repository:\n" +
        "lib/ @lib-owner\n" +
        "\n" +
        "# This code match only a `config` directory in the root of the repository:\n" +
        "/config/ @config-owner\n" +
        "\n" +
        "# If the path contains spaces, escape them like this:\n" +
        "path\\ with\\ spaces/ @space-owner\n" +
        "\n" +
        "# Code Owners section:\n" +
        "[Documentation]\n" +
        "ee/docs    @docs\n" +
        "docs       @docs\n" +
        "\n" +
        "# Use of default owners for a section. In this case, all files (*) are owned by\n" +
        "# the dev team except the README.md and data-models which are owned by other teams.\n" +
        "[Development] @dev-team\n" +
        "*\n" +
        "README.md @docs-team\n" +
        "data-models/ @data-science-team\n" +
        "\n" +
        "# This section is combined with the previously defined [Documentation] section:\n" +
        "[DOCUMENTATION]\n" +
        "README.md  @docs";

    @Test
    void testCodeOwnersToString() {
        CodeOwners codeOwners = new CodeOwners(CODEOWNERS);
        LOG.info("\n{}", codeOwners);
        runChecks(codeOwners);
        // Now reparse the toString output...
        CodeOwners codeOwners2 = new CodeOwners(codeOwners.toString());
        runChecks(codeOwners2);
    }

    private void runChecks(CodeOwners codeOwners) {
        assertOwners(codeOwners, "Foo.txt","@code","@multiple","@owners", "@dev-team");
        assertOwners(codeOwners, "Foo.rb","@ruby-owner", "@dev-team");
        assertOwners(codeOwners, "#file_with_pound.rb","@owner-file-with-pound", "@dev-team");
    }

}
