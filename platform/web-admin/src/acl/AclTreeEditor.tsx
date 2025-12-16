import { AclTreeEditorSkeleton } from 'admin/src-gen/acl/AclTreeEditorSkeleton';
import { Col, Row, Tree, TreeDataNode, Typography } from 'antd';
import { AclMetadataElementWrapper } from 'admin/src-gen/acl/AclMetadataElementWrapper';
import { initStateSetters } from 'admin/src/common/component';
const { Text } = Typography;

function AclTreeEditorFC(props: { element: AclTreeEditorComponent }) {
    initStateSetters(props.element);
    const hasRulesIds = props.element.getHasRulesIds() || [];
    const hasChildrenWithRulesIds = props.element.getHasChildrenWithRulesIds() || [];
    const convertToDataNode = (entry: AclMetadataElementWrapper) => {
        const item: TreeDataNode = {
            title:
                hasRulesIds.indexOf(entry.id) !== -1 ? (
                    <Text type="success">{entry.name}</Text>
                ) : hasChildrenWithRulesIds.indexOf(entry.id) !== -1 ? (
                    <Text type="warning">{entry.name}</Text>
                ) : (
                    <Text>{entry.name}</Text>
                ),
            key: entry.id,
            children: [],
        };
        if (entry.children?.length) {
            entry.children.forEach((ch) => item.children!.push(convertToDataNode(ch)));
        }
        return item;
    };
    const treeData: TreeDataNode[] = [convertToDataNode(props.element.getRootEntry()!)];
    const expandedNodesIds = [treeData[0].key];
    treeData[0].children!.forEach((ch) => expandedNodesIds.push(ch.key));
    return (
        <>
            <div style={{ flexGrow: 0, flexShrink: 0, height: '100%', padding: '5px' }}>
                <Tree
                    showLine={true}
                    showIcon={true}
                    defaultExpandedKeys={expandedNodesIds}
                    onSelect={(n) => {
                        if (!n || !n[0]) {
                            return;
                        }
                        props.element.sendSelectNode({
                            nodeId: n[0] as string,
                        });
                    }}
                    selectedKeys={[props.element.getSelectedNodeId()]}
                    treeData={treeData}
                />
            </div>
            <div style={{ flexGrow: 1, height: '100%', padding: '5px' }}>
                <Row style={{ width: '100%' }}>
                    <Col xs={23}>
                        {props.element.findByTag('entry').createReactElement()}
                    </Col>
                </Row>
            </div>
        </>
    );
}

export class AclTreeEditorComponent extends AclTreeEditorSkeleton {
    functionalComponent = AclTreeEditorFC;
}
