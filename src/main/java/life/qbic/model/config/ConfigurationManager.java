/*******************************************************************************
 * QBiC User DB Tools enables users to add people and affiliations to our mysql user database.
 * Copyright (C) 2016  Andreas Friedrich
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package life.qbic.model.config;

import java.util.List;

/**
 * The ConfigurationManger interface represents the entire .properties file. One might think about
 * adding a getAttribute method in order to make it more generic.
 *
 * @author wojnar
 *
 */
public interface ConfigurationManager {

    String getVocabularyMSLabeling();

    String getConfigurationFileName();

    String getDataSourceUser();

    String getDataSourcePassword();

    String getDataSourceUrl();

    String getBarcodeScriptsFolder();

    String getTmpFolder();

    String getBarcodePathVariable();

    String getAttachmentURI();

    String getAttachmentUser();

    String getAttachmenPassword();

    String getAttachmentMaxSize();

    String getMysqlHost();

    String getMysqlPort();

    String getMysqlDB();

    String getMysqlUser();

    String getMysqlPass();

    String getLdapHost();
    String getLdapBase();
    String getLdapUser();
    String getLdapPass();

    List<String> getDBInputUserGrps();

    List<String> getDBInputAdminGrps();
}
