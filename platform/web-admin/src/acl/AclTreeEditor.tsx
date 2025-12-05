import { AclTreeEditorSkeleton } from 'admin/src-gen/acl/AclTreeEditorSkeleton';
import { Col, Row, Tree, TreeDataNode } from 'antd';
import { AclMetadataElementWrapper } from 'admin/src-gen/acl/AclMetadataElementWrapper';

function AclTreeEditorFC(props: { element: AclTreeEditorComponent }) {
    const convertToDataNode = (entry: AclMetadataElementWrapper) => {
        const item: TreeDataNode = {
            title: entry.name,
            key: entry.id,
            children: [],
        };
        if (entry.children?.length) {
            entry.children.forEach((ch) => item.children!.push(convertToDataNode(ch)));
        }
        return item;
    };
    const treeData: TreeDataNode[] = [convertToDataNode(props.element.getRootEntry()!)];
    return (
        <>
            <div style={{ flexGrow: 0, height: '100%', padding: '5px' }}>
                <Tree
                    showLine={true}
                    showIcon={true}
                    defaultExpandAll
                    selectedKeys={[props.element.getSelectedNodeId()]}
                    treeData={treeData}
                />
            </div>
            <div style={{ flexGrow: 1, height: '100%', padding: '5px' }}>
                <Row style={{ width: '100%' }}>
                    <Col xs={24} sm={12} style={{ width: '100%' }}>
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
