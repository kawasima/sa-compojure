/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.struts.creator;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

/**
 * {@link ActionForm}に相当するクラス用の{@link ComponentCreator}です。
 * <p>
 * 決められた命名規約に従って、クラスからActionFormクラスのコンポーネント定義を作成します。 作成されるコンポーネント定義の各種属性は以下になります。
 *
 * <table>
 * <tr>
 * <th>サフィックス</th>
 * <td>"Form"</td>
 * </tr>
 * <tr>
 * <th>インスタンス定義</th>
 * <td>request</td>
 * </tr>
 * <tr>
 * <th>自動バインディング</th>
 * <td>無効</td>
 * </tr>
 * <tr>
 * <th>外部バインディング</th>
 * <td>無効</td>
 * </tr>
 * <tr>
 * <th>インターフェース</th>
 * <td>対象外</td>
 * </tr>
 * <tr>
 * <th>抽象クラス</th>
 * <td>対象外</td>
 * </tr>
 * </table>
 * </p>
 *
 * @author dewa
 */
public class FormCreator extends ComponentCreatorImpl {

    /**
     * 指定された{@link NamingConvention 命名規約}に従った{@link FormCreator}を作成します。
     *
     * @param namingConvention
     *            命名規約
     */
    public FormCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setNameSuffix("Form");
        setInstanceDef(InstanceDefFactory.REQUEST);
        setAutoBindingDef(AutoBindingDefFactory.NONE);
    }

    /**
     * {@link ComponentCustomizer}を返します。
     *
     * @return {@link ComponentCustomizer}
     */
    public ComponentCustomizer getFormCustomizer() {
        return getCustomizer();
    }

    /**
     * {@link ComponentCustomizer}を設定します。
     *
     * @param customizer
     *            {@link ComponentCustomizer}
     */
    public void setFormCustomizer(ComponentCustomizer customizer) {
        setCustomizer(customizer);
    }

}
